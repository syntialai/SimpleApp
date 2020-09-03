package com.blibli.simpleapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blibli.simpleapp.R
import com.blibli.simpleapp.data.User
import com.blibli.simpleapp.util.ImageHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var users: ArrayList<User> = arrayListOf()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickedCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fragment_user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]

        holder.tvUserName.text = item.login
        ImageHelper.resizeAndBuildImage(
            holder.itemView.context,
            item.avatar_url,
            holder.ivUserImage,
            R.dimen.image_user_size
        )

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(users[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = users.size

    fun updateList(userslist: ArrayList<User>) {
        users = userslist
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: MaterialTextView = view.findViewById(R.id.tv_user_name)
        val ivUserImage: ShapeableImageView = view.findViewById(R.id.iv_user_image)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}