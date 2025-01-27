package com.achelm.helloworld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.achelm.helloworld.R
import com.achelm.helloworld.controllers.CommunityAdapter
import com.achelm.helloworld.models.CommunityModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class CommunityFragment: Fragment() {

    private lateinit var view: View
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private var myAdapter: CommunityAdapter? = null
    private var listOfCommunity: ArrayList<CommunityModel>? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var noMatchingUsersTxtView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_community, container, false)

        // Initialize variables
        toolbar = view.findViewById(R.id.communityFragment_toolBarId)
        recyclerView = view.findViewById(R.id.communityFragment_recyclerViewId)
        noMatchingUsersTxtView = view.findViewById(R.id.noMatchingUsersTextViewId)

        // Set menu items in Toolbar
        toolbar.inflateMenu(R.menu.menu_of_toolbar_community)
        // Handle menu item clicks
        itemsOfToolbar()

        firebaseAuth = FirebaseAuth.getInstance()

        gettingDataFromFbProcess()

        return view
    }

    private fun itemsOfToolbar() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.searchViewId -> {
                    // Handle search item click

                    true
                }
                else -> false
            }
        }
    }

    private fun gettingDataFromFbProcess() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listOfCommunity = ArrayList()

        var userReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseAuth.uid!!)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var id: String = snapshot.child("id").getValue(String::class.java)!!
                    var nativeLanguage: String = snapshot.child("nativeLanguage").getValue(String::class.java)!!
                    var learningLanguage: String = snapshot.child("learningLanguage").getValue(String::class.java)!!

                    var query: Query = FirebaseDatabase.getInstance().reference.child("Users")
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            listOfCommunity!!.clear()

                            for ( userSnapshot: DataSnapshot in dataSnapshot.children) {

                                val userId = userSnapshot.child("id").getValue(String::class.java).orEmpty()
                                val userNativeLanguage = userSnapshot.child("nativeLanguage").getValue(String::class.java).orEmpty()
                                val userLearningLanguage = userSnapshot.child("learningLanguage").getValue(String::class.java).orEmpty()

                                var communityModel = CommunityModel()

                                if ( userId != id ) {
                                    communityModel.id = userSnapshot.child("id").value.toString()
                                    communityModel.fullname = userSnapshot.child("fullname").value.toString()
                                    communityModel.age = userSnapshot.child("age").value.toString()
                                    communityModel.profilePic = userSnapshot.child("profilePic").value.toString()
                                    communityModel.nationality = userSnapshot.child("nationality").value.toString()
                                    communityModel.nativeLanguage = userSnapshot.child("nativeLanguage").value.toString()
                                    communityModel.learningLanguage = userSnapshot.child("learningLanguage").value.toString()

                                    listOfCommunity!!.add(communityModel)
                                }

//                                if ( nativeLanguage == userLearningLanguage && learningLanguage == userNativeLanguage ) {
//                                    communityModel.id = userSnapshot.child("id").value.toString()
//                                    communityModel.fullname = userSnapshot.child("fullname").value.toString()
//                                    communityModel.age = userSnapshot.child("age").value.toString()
//                                    communityModel.profilePic = userSnapshot.child("profilePic").value.toString()
//                                    communityModel.nationality = userSnapshot.child("nationality").value.toString()
//                                    communityModel.nativeLanguage = userSnapshot.child("nativeLanguage").value.toString()
//                                    communityModel.learningLanguage = userSnapshot.child("learningLanguage").value.toString()
//
//                                    listOfCommunity!!.add(communityModel)
//                                }
                            }

                            if (listOfCommunity!!.isEmpty()) {
                                recyclerView.visibility = View.GONE
                                noMatchingUsersTxtView.visibility = View.VISIBLE
                            } else {
                                noMatchingUsersTxtView.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            }

                            myAdapter = CommunityAdapter(requireContext(), listOfCommunity!!)
                            recyclerView.adapter = myAdapter
                            myAdapter!!.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })

                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}