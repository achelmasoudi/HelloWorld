package com.achelm.helloworld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.achelm.helloworld.R
import com.achelm.helloworld.controllers.ChatsAdapter
import com.achelm.helloworld.models.CommunityModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatsFragment : Fragment() {

    private lateinit var view: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var noConversations: LinearLayout
    private var chatsAdapter: ChatsAdapter? = null
    private var chatUserList: ArrayList<CommunityModel>? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var currentUserUid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_chats, container, false)

        // Initialize variables
        recyclerView = view.findViewById(R.id.chatsFragment_recyclerViewId)
        noConversations = view.findViewById(R.id.chatsFragment_noConversationsId)

        firebaseAuth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference
        currentUserUid = firebaseAuth.currentUser!!.uid

        getChatUsersFromFb()

        return view
    }

    private fun getChatUsersFromFb() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatUserList = ArrayList()

        // Retrieve chat users from ChatsHistory
        val chatHistoryRef = database.child("ChatsHistory").child(currentUserUid)

        chatHistoryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatUserList!!.clear()
                for (userSnapshot in snapshot.children) {
                    val chatUser = userSnapshot.getValue(CommunityModel::class.java)
                    if (chatUser != null) {
                        chatUserList!!.add(chatUser)
                    }
                }

                if (chatUserList!!.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    noConversations.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    noConversations.visibility = View.GONE
                }

                chatsAdapter = ChatsAdapter(requireContext(), chatUserList!!)
                recyclerView.adapter = chatsAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })

        // Listen for new messages in real-time and update UI
        val chatsRef = database.child("Chats")
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // You can update chatUserList here if needed
                chatsAdapter?.notifyDataSetChanged()  // Update the adapter if new data arrives
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

}