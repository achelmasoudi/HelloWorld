package com.achelm.helloworld.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.achelm.helloworld.R
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileActivity : AppCompatActivity() {

    private lateinit var loadingAnimation: LottieAnimationView

    private var fullnameTextView: TextView? = null
    private var profilePicImageView: CircleImageView? = null
    private var nativeLangTextView: TextView? = null
    private var learningLangTextView: TextView? = null
    private var nationalityTextView: TextView? = null
    private var ageTextView: TextView? = null

    private lateinit var userProfilePicture: String
    private lateinit var uid: String
    private lateinit var fullname: String
    private lateinit var nativeLang: String
    private lateinit var learningLang: String
    private lateinit var nationality: String
    private lateinit var age: String

    private lateinit var toolbar: Toolbar

    private var sendMsgBtn: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toolbar = findViewById(R.id.userProfileActivity_toolBarId)

        // Set arrow back button to Toolbar
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //get the data from the other activities
        getDataFromActivitiesAndSetItHere()

        sendMessageProcess()

        profilePicImageView!!.setOnClickListener {
            showDialogOfProfilePic()
        }
    }

    private fun showDialogOfProfilePic() {
        val bottomSheetViewOfShowProfilePic: View = LayoutInflater.from(this).inflate( R.layout.bottom_sheet_of_show_profilepic,
            this.findViewById(R.id.bottomSheetLayout_showProfilePic_containerId) )


        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetViewOfShowProfilePic)

        // Set up BottomSheetDialog's behavior
        val bottomSheetBehavior: BottomSheetBehavior<FrameLayout> = bottomSheetDialog.behavior
        bottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels // hadi bach tchad the whole screen

        bottomSheetDialog.show()

        // Initialize the variables of Bottom Sheet and set data to them
        val profilePicImageView: CircleImageView = bottomSheetViewOfShowProfilePic.findViewById(R.id.bottomSheetLayout_showProfilePic_profilePicId)


        Glide.with(this).load(userProfilePicture).apply(RequestOptions.fitCenterTransform()).into(profilePicImageView)
    }

    private fun getDataFromActivitiesAndSetItHere() {
        // Initialize the variables
        loadingAnimation = findViewById(R.id.userProfileActivity_loadingLottie)

        fullnameTextView = findViewById(R.id.userProfileActivity_fullnameId)
        profilePicImageView = findViewById(R.id.userProfileActivity_profilePicId)
        nativeLangTextView = findViewById(R.id.userProfileActivity_nativeLangId)
        learningLangTextView = findViewById(R.id.userProfileActivity_learningLangId)
        nationalityTextView = findViewById(R.id.userProfileActivity_nationalityId)
        ageTextView = findViewById(R.id.userProfileActivity_ageId)

        var bundle: Bundle = intent.extras!!

        if(bundle != null) {
            uid = bundle.getString("uid")!!
            fullname = bundle.getString("fullname")!!
            age = bundle.getString("age")!!
            userProfilePicture = bundle.getString("profilePic")!!
            nationality = bundle.getString("nationality")!!
            nativeLang = bundle.getString("nativeLanguage")!!
            learningLang = bundle.getString("learningLanguage")!!

            Glide.with(this).load(userProfilePicture).apply(RequestOptions.fitCenterTransform())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        loadingAnimation.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        // Image loaded successfully
                        loadingAnimation.visibility = View.GONE
                        return false
                    }
                }).into(profilePicImageView!!)

            toolbar.title = fullname
            fullnameTextView!!.text = fullname
            ageTextView!!.text = age
            nationalityTextView!!.text = nationality
            nativeLangTextView!!.text = nativeLang
            learningLangTextView!!.text = learningLang
        }
    }

    private fun sendMessageProcess() {
        sendMsgBtn = findViewById(R.id.userProfileActivity_sendMsgId)
        sendMsgBtn!!.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)

            intent.putExtra("uid" , uid)
            intent.putExtra("fullname" , fullname)
            intent.putExtra("age", age)
            intent.putExtra("profilePic" , userProfilePicture)
            intent.putExtra("nationality" , nationality)
            intent.putExtra("nativeLanguage" , nativeLang)
            intent.putExtra("learningLanguage" , learningLang)
            intent.putExtra("receiverUid" , FirebaseAuth.getInstance().currentUser!!.uid)
            startActivity(intent)
        }
    }
}