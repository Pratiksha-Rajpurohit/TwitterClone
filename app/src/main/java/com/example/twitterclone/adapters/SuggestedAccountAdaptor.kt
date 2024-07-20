package com.example.twitterclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.R
import com.example.twitterclone.data.SuggestedAccount
import de.hdodenhof.circleimageview.CircleImageView

class SuggestedAccountAdaptor(
        private val listofsuggestedAccounts : List<SuggestedAccount>,
        private val context : Context,
        private val clicklisterner : Clicklisterner

) : RecyclerView.Adapter<SuggestedAccountAdaptor.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val image : CircleImageView = itemView.findViewById(R.id.suggested_account_profile_image)
        val name : TextView = itemView.findViewById(R.id.suggested_account_name)
        val btnFollow : Button = itemView.findViewById(R.id.btn_follow)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_suggested_account,parent,false)
    }

    override fun getItemCount(): Int {
        return listofsuggestedAccounts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSuggestedAccount = listofsuggestedAccounts[position]

        holder.name.text = currentSuggestedAccount.username

        Glide.with(context)
            .load(currentSuggestedAccount.profileImage)
            .into(holder.image)

        holder.btnFollow.setOnClickListener {
            clicklisterner.onFollowClicked()
        }

    }

    interface Clicklisterner{

        fun onFollowClicked()

    }

}