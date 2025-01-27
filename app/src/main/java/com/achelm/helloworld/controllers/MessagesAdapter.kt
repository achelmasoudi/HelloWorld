package com.achelm.helloworld.controllers

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.achelm.helloworld.R
import com.achelm.helloworld.models.Message
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessagesAdapter(var context: Activity, listOfMessages: ArrayList<Message> , senderRoom: String , receiverRoom: String) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    lateinit var listOfMessages: ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    val senderRoom: String
    val receiverRoom: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.send_message, parent, false)
            SentMsgHolder(view)
        }
        else {
            val view = LayoutInflater.from(context).inflate(R.layout.receive_message, parent, false)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = listOfMessages[position]
        return if (FirebaseAuth.getInstance().uid == message.senderId) {
            ITEM_SENT
        }
        else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int = listOfMessages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = listOfMessages[position]
        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder
            viewHolder.sendMessage.text = message.msg
            viewHolder.sendMessageTime.text = message.timeStamp
            viewHolder.itemView.setOnClickListener {
                var bottomSheetView: View = LayoutInflater.from(context).inflate(R.layout.bottomsheet_of_message,
                    context.findViewById(R.id.bottomSheetLayout_message_containerId))

                var bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                // Set the date of message
                var msgDate: TextView? = bottomSheetDialog.findViewById(R.id.messageTimeId)
                msgDate!!.text = message.fullDate

                // Edit message
                var editBtn: CardView? = bottomSheetDialog.findViewById(R.id.editMsgBtnId)
                editBtn!!.setOnClickListener {

                }

                // Delete message
                var deleteBtn: CardView? = bottomSheetDialog.findViewById(R.id.deleteMsgBtnId)
                deleteBtn!!.setOnClickListener {
                    bottomSheetDialog.dismiss()

                    val alertDialog = MaterialAlertDialogBuilder(context)
                        .setTitle("DELETE")
                        .setMessage("Are you sure you want to delete this message ?")
                        .setPositiveButton("DELETE") { _, _ ->

                            deleteMessageProcess(message)

                            Snackbar.make( context , deleteBtn, "You have successfully deleted this message.", Snackbar.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, i ->
                            dialogInterface.cancel()
                        }
                        .create()
                    alertDialog.show()
                }
            }
        }
        else {
            val viewHolder = holder as ReceiveMsgHolder
            viewHolder.receiveMessage.text = message.msg
            viewHolder.receiveMessageTime.text = message.timeStamp
            viewHolder.itemView.setOnClickListener {
                // Do something like delete or edit...
            }
        }
    }

    private fun deleteMessageProcess(message: Message) {
        val messageKey = message.msgId
        val senderRoomRef = FirebaseDatabase.getInstance().reference.child("Chats").child(senderRoom).child("message").child(messageKey)
        val receiverRoomRef = FirebaseDatabase.getInstance().reference.child("Chats").child(receiverRoom).child("message").child(messageKey)

        val senderUid = message.senderId
        val receiverUid = message.receiverId

        senderRoomRef.removeValue().addOnSuccessListener {
            receiverRoomRef.removeValue().addOnSuccessListener {
                // Remove the chat history entry for both sender and receiver
                FirebaseDatabase.getInstance().reference.child("ChatsHistory").child(senderUid).child(receiverUid)
                    .removeValue().addOnSuccessListener {
                        // If successful, update your local list and notify adapter
                        listOfMessages.remove(message)
                        notifyDataSetChanged()

                        Snackbar.make(context.findViewById(android.R.id.content), "Message deleted successfully", Snackbar.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        Snackbar.make(context.findViewById(android.R.id.content), "Failed to delete message: ${exception.message}", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener { exception ->
            Snackbar.make(context.findViewById(android.R.id.content), "Failed to delete message: ${exception.message}", Snackbar.LENGTH_SHORT).show()
        }
    }

    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sendMessage: TextView = itemView.findViewById(R.id.sendMessage_msgId)
        var sendMessageTime: TextView = itemView.findViewById(R.id.sendMessageTime_msgId)
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receiveMessage: TextView = itemView.findViewById(R.id.receiveMessage_msgId)
        var receiveMessageTime: TextView = itemView.findViewById(R.id.receiveMessageTime_msgId)
    }

    init {
        if (listOfMessages != null) {
            this.listOfMessages = listOfMessages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }
}