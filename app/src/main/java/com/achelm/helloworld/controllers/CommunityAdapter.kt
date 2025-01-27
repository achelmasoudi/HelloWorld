package com.achelm.helloworld.controllers

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.achelm.helloworld.R
import com.achelm.helloworld.activities.UserProfileActivity
import com.achelm.helloworld.models.CommunityModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class CommunityAdapter(var context: Context, var listOfCommunity: List<CommunityModel> ) : RecyclerView.Adapter<CommunityAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.community_card_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = listOfCommunity.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var communityModel = listOfCommunity.get(position)
        holder.fullnameTxtView.text = communityModel.fullname
        holder.ageTxtView.text = communityModel.age
        holder.nativeLangTxtView.text = communityModel.nativeLanguage
        holder.learningLangTxtView.text = communityModel.learningLanguage

        // Glide for getting the profile picture from Fb
        Glide.with(context).load(communityModel.profilePic)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    return false
                }
                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    // Image loaded successfully
                    return false
                }
            }).into(holder.profilePicImageView)

        holder.button.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)

            intent.putExtra("uid" , communityModel.id)
            intent.putExtra("fullname" , communityModel.fullname)
            intent.putExtra("age", communityModel.age)
            intent.putExtra("profilePic" , communityModel.profilePic)
            intent.putExtra("nationality" , communityModel.nationality)
            intent.putExtra("nativeLanguage" , communityModel.nativeLanguage)
            intent.putExtra("learningLanguage" , communityModel.learningLanguage)

            context.startActivity(intent)
        }

    }

    inner class MyViewHolder(i: View) : RecyclerView.ViewHolder(i){
        val button: CardView = i.findViewById(R.id.communityCardItem_btnId)
        val profilePicImageView: CircleImageView = i.findViewById(R.id.communityCardItem_profilePicId)
        val fullnameTxtView: TextView = i.findViewById(R.id.communityCardItem_fullnameId)
        val ageTxtView: TextView = i.findViewById(R.id.communityCardItem_ageId)
        val nativeLangTxtView: TextView = i.findViewById(R.id.communityCardItem_nativeLanId)
        val learningLangTxtView: TextView = i.findViewById(R.id.communityCardItem_learningLanId)
    }
}