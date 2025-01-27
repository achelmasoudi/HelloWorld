package com.achelm.helloworld.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.achelm.helloworld.R
import com.achelm.helloworld.fragments.ChatsFragment
import com.achelm.helloworld.fragments.CommunityFragment
import com.achelm.helloworld.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigView = findViewById(R.id.mainActivity_bottomNavigationViewId)

        //Default Fragment is (Community fragment)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_containerId, ChatsFragment()).addToBackStack(null).commit()

        bottomNavigView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when(item.itemId) {
                R.id.bottomNav_chatsId -> selectedFragment = ChatsFragment()
                R.id.bottomNav_communityId -> selectedFragment = CommunityFragment()
                R.id.bottomNav_profileId -> selectedFragment = ProfileFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_containerId, selectedFragment!!).addToBackStack(null).commit()
            true
        }

    }
}