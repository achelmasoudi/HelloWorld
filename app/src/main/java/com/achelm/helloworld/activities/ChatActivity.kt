package com.achelm.helloworld.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.achelm.helloworld.R
import com.achelm.helloworld.controllers.MessagesAdapter
import com.achelm.helloworld.models.CommunityModel
import com.achelm.helloworld.models.Message
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private var myAdapter: MessagesAdapter? = null
    private var listOfMessages: ArrayList<Message>? = null
    private var senderRoom: String? = null
    private var receiverRoom: String? = null

    private lateinit var database: FirebaseDatabase

    private var senderUid: String? = null
    private var receiverUid: String? = null

    private var fullnameTextView: TextView? = null
    private var profilePicImageView: CircleImageView? = null
    private var statusTextView: TextView? = null
    private var messageBoxEditTxt: EditText? = null
    private var sendMsgBtn: ImageView? = null
    private var arrowBack: ImageView? = null

    private lateinit var userProfilePicture: String
    private lateinit var fullname: String
    private lateinit var nativeLang: String
    private lateinit var learningLang: String
    private lateinit var nationality: String
    private lateinit var age: String

    private lateinit var goToUserProfile: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.chatActivity_toolBarId)
        setSupportActionBar(toolbar)

        // Initialize the variables
        arrowBack = findViewById(R.id.chatActivity_arrowBackId)
        profilePicImageView = findViewById(R.id.chatActivity_profilePicId)
        fullnameTextView = findViewById(R.id.chatActivity_fullnameId)
        statusTextView = findViewById(R.id.chatActivity_statusId)
        recyclerView = findViewById(R.id.chatActivity_recyclerViewId)
        messageBoxEditTxt = findViewById(R.id.chatActivity_messageBoxId)
        sendMsgBtn = findViewById(R.id.chatActivity_sendMsgBtnId)
        goToUserProfile = findViewById(R.id.chatActivity_goToUserProfileId)

        arrowBack!!.setOnClickListener { finish() }

        database = FirebaseDatabase.getInstance()

        listOfMessages = ArrayList()

        var bundle: Bundle = intent.extras!!

        if(bundle != null) {
            receiverUid = bundle.getString("uid")!!
            fullname = bundle.getString("fullname")!!
            age = bundle.getString("age")!!
            userProfilePicture = bundle.getString("profilePic")!!
            nationality = bundle.getString("nationality")!!
            nativeLang = bundle.getString("nativeLanguage")!!
            learningLang = bundle.getString("learningLanguage")!!

            Glide.with(this).load(userProfilePicture).apply(RequestOptions.fitCenterTransform())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }
                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        return false
                    }
                }).into(profilePicImageView!!)

            fullnameTextView!!.text = fullname
            senderUid = FirebaseAuth.getInstance().uid
            database.reference.child("Presence").child(receiverUid!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val status = snapshot.getValue(String::class.java)
                            if (status == "offline") {
                                statusTextView!!.visibility = View.GONE
                            }
                            else {
                                statusTextView!!.visibility = View.VISIBLE
                                statusTextView!!.text = status
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                } )

            senderRoom = senderUid + receiverUid
            receiverRoom = receiverUid + senderUid

            myAdapter = MessagesAdapter(this , listOfMessages!! , senderRoom!! , receiverRoom!!)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = myAdapter

            database.reference.child("Chats")
                .child(senderRoom!!)
                .child("message")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listOfMessages!!.clear()
                        for (snapshot1 in snapshot.children) {
                            val message: Message = snapshot1.getValue(Message::class.java)!!
                            listOfMessages!!.add(message)
                        }
                        myAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

            sendMsgBtn!!.setOnClickListener {
                val msgText = messageBoxEditTxt!!.text.toString()
                if (msgText.isNotEmpty()) {
                    val date = Date()

                    // Format time for display
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val formattedTime = timeFormat.format(date)

                    // Format full date and time for storage
                    val fullDateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
                    val formattedDateTime = fullDateFormat.format(date)

                    // Generate a unique message ID
                    val randomKey = database.reference.push().key ?: ""

                    val message = Message(
                        msg = msgText ,
                        msgId = randomKey ,
                        senderId = senderUid!! ,
                        receiverId = receiverUid!! ,
                        timeStamp = "$formattedTime" ,
                        fullDate = "$formattedDateTime"
                    )

                    messageBoxEditTxt!!.setText("")

                    // Update the last message and timestamp for both sender and receiver rooms
                    val senderRoomRef = database.reference.child("Chats").child(senderRoom!!)
                    val receiverRoomRef = database.reference.child("Chats").child(receiverRoom!!)

                    senderRoomRef.child("message").child(randomKey).setValue(message).addOnSuccessListener {
                        // Add user to the chat history
                        val chatUserSender = CommunityModel(receiverUid!!, fullname, age, userProfilePicture, nationality, nativeLang, learningLang, message.msg, message.timeStamp)
                        database.reference.child("ChatsHistory").child(senderUid!!).child(receiverUid!!).setValue(chatUserSender)
                    }

                    receiverRoomRef.child("message").child(randomKey).setValue(message)

                }
            }

            val handler = Handler()
            messageBoxEditTxt!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    database.reference.child("Presence")
                        .child(senderUid!!)
                        .setValue("typing...")
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(userStoppedTyping , 1000)
                }
                var userStoppedTyping = Runnable {
                    database.reference.child("Presence")
                        .child(senderUid!!)
                        .setValue("online")
                }
            })
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        // Go to user profile
        goToUserProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)

            intent.putExtra("uid" , receiverUid)
            intent.putExtra("fullname" , fullname)
            intent.putExtra("age", age)
            intent.putExtra("profilePic" , userProfilePicture)
            intent.putExtra("nationality" , nationality)
            intent.putExtra("nativeLanguage" , nativeLang)
            intent.putExtra("learningLanguage" , learningLang)

            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database.reference.child("Presence")
            .child(currentId!!)
            .setValue("offline")
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database.reference.child("Presence")
            .child(currentId!!)
            .setValue("online")
    }
}