package com.achelm.helloworld.controllers

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.achelm.helloworld.R
import com.achelm.helloworld.activities.ChatActivity
import com.achelm.helloworld.models.CommunityModel
import com.achelm.helloworld.models.Message
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatsAdapter(var context: Context, var chatUserList: ArrayList<CommunityModel>) : RecyclerView.Adapter<ChatsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.community_card_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = chatUserList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var chatModel = chatUserList.get(position)
        holder.fullnameTxtView.text = chatModel.fullname
        holder.ageTxtView.visibility = View.GONE

        // I will do this for Last Message !
        holder.lastMsg.text = chatModel.lastMsg
        holder.lastMsg.setTextColor(context.resources.getColor(R.color.gray))
        holder.learningLangTxtView.visibility = View.GONE
        holder.iconFromTo.visibility = View.GONE

        holder.lastMsgTime.visibility = View.VISIBLE
        holder.lastMsgTime.text = chatModel.lastMsgTime


        // Glide for getting the profile picture from Fb
        Glide.with(context).load(chatModel.profilePic)
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
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("uid" , chatModel.id)
            intent.putExtra("fullname" , chatModel.fullname)
            intent.putExtra("age", chatModel.age)
            intent.putExtra("profilePic" , chatModel.profilePic)
            intent.putExtra("nationality" , chatModel.nationality)
            intent.putExtra("nativeLanguage" , chatModel.nativeLanguage)
            intent.putExtra("learningLanguage" , chatModel.learningLanguage)

            context.startActivity(intent)
        }
    }

    inner class MyViewHolder(i: View) : RecyclerView.ViewHolder(i){
        val button: CardView = i.findViewById(R.id.communityCardItem_btnId)
        val profilePicImageView: CircleImageView = i.findViewById(R.id.communityCardItem_profilePicId)
        val fullnameTxtView: TextView = i.findViewById(R.id.communityCardItem_fullnameId)
        val ageTxtView: TextView = i.findViewById(R.id.communityCardItem_ageId)
        val learningLangTxtView: TextView = i.findViewById(R.id.communityCardItem_learningLanId)
        val iconFromTo: ImageView = i.findViewById(R.id.communityCardItem_iconfromtoId)
        val lastMsg: TextView = i.findViewById(R.id.communityCardItem_nativeLanId)
        val lastMsgTime: TextView = i.findViewById(R.id.communityCardItem_lastMsgTimeId)
    }

}