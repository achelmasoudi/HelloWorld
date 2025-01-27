package com.achelm.helloworld.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.achelm.helloworld.R
import com.achelm.helloworld.activities.LoginActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var view: View
    private lateinit var loadingAnimation: LottieAnimationView

    companion object {
        private const val EDIT_PROFILE_REQUEST_CODE = 1001
    }

    private var mAuth: FirebaseAuth? = null
    private var fullnameTextView: TextView? = null
    private var profilePicImageView: CircleImageView? = null
    private var nativeLangTextView: TextView? = null
    private var learningLangTextView: TextView? = null
    private var nationalityTextView: TextView? = null
    private var ageTextView: TextView? = null

    private lateinit var userProfilePicture: String
    private lateinit var fullname: String
    private lateinit var nativeLang: String
    private lateinit var learningLang: String
    private lateinit var nationality: String
    private lateinit var age: String

    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_profile, container, false)

        loadingAnimation = view.findViewById(R.id.profileFragment_loadingLottie)

        mAuth = FirebaseAuth.getInstance()

        toolbar = view.findViewById(R.id.profileFragment_toolBarId)
        // Set menu items in Toolbar
        toolbar.inflateMenu(R.menu.menu_of_toolbar_profile)
        // Handle menu item clicks
        itemsOfToolbar()

        getDataFromFb()

        return view
    }

    private fun getDataFromFb() {
        //Initialize the variables
        fullnameTextView = view.findViewById(R.id.profileFragment_fullnameId)
        profilePicImageView = view.findViewById(R.id.profileFragment_profilePicId)
        nativeLangTextView = view.findViewById(R.id.profileFragment_nativeLangId)
        learningLangTextView = view.findViewById(R.id.profileFragment_learningLangId)
        nationalityTextView = view.findViewById(R.id.profileFragment_nationalityId)
        ageTextView = view.findViewById(R.id.profileFragment_ageId)

        var reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth!!.currentUser!!.uid)

        // Use ValueEventListener to get the value of the "fullname" child
        reference.child("fullname").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the value from the dataSnapshot
                fullname = snapshot.getValue(String::class.java)!!
                fullnameTextView!!.text = fullname
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        // Use ValueEventListener to get the value of the "native language" child
        reference.child("nativeLanguage").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the value from the dataSnapshot
                nativeLang = snapshot.getValue(String::class.java)!!
                nativeLangTextView!!.text = nativeLang
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        // Use ValueEventListener to get the value of the "learning language" child
        reference.child("learningLanguage").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the value from the dataSnapshot
                learningLang = snapshot.getValue(String::class.java)!!
                learningLangTextView!!.text = learningLang
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        reference.child("nationality").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the value from the dataSnapshot
                nationality = snapshot.getValue(String::class.java)!!
                nationalityTextView!!.text = nationality
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        reference.child("age").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the value from the dataSnapshot
                age = snapshot.getValue(String::class.java)!!
                ageTextView!!.text = age
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        reference.child("profilePic").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the value from the dataSnapshot
                userProfilePicture = snapshot.getValue(String::class.java)!!

                context?.let {
                    Glide.with(it).load(userProfilePicture).listener(object :
                        RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            // Image loaded successfully
                            loadingAnimation.visibility = View.GONE

                            return false
                        }
                    }).into(profilePicImageView!!)
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun itemsOfToolbar() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settingsId -> {
                    settingsProcess()
                    true
                }
                else -> false
            }
        }
    }

    private fun settingsProcess() {
        var bottomSheetView: View = LayoutInflater.from(activity).inflate(R.layout.bottomsheet_of_settings,
            requireActivity().findViewById(R.id.bottomSheetLayout_settings_containerId))

        var bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        // Edit profile
        var editBtn: CardView? = bottomSheetDialog.findViewById(R.id.profileFragment_editProfileBtnId)
        editBtn!!.setOnClickListener {

        }

        // Log out
        var logoutBtn: CardView? = bottomSheetDialog.findViewById(R.id.profileFragment_logOutBtnId)
        logoutBtn!!.setOnClickListener {
            bottomSheetDialog.dismiss()

            val alertDialog = MaterialAlertDialogBuilder(requireActivity())
                .setTitle("LOG OUT")
                .setMessage("Are you sure you want to log out ?")
                .setPositiveButton("LOG OUT") { _, _ ->
                    mAuth!!.signOut()
                    var intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    activity?.finish()

                    Snackbar.make( requireActivity() , logoutBtn, "You have successfully logged out.", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.cancel()
                }
                .create()
            alertDialog.show()

            // for when the user log out he can again signup but he can't enter to mainAc until login
            clearLoginFlag()
        }

        // Delete account
        var deleteAccountBtn: CardView? = bottomSheetDialog.findViewById(R.id.profileFragment_deleteAccBtnId)
        deleteAccountBtn!!.setOnClickListener {

        }
    }

    private fun clearLoginFlag() {
        val prefs: SharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.remove("loginCompleted")
        editor.apply()
    }



}