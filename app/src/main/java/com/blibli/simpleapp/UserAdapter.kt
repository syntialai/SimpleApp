package com.blibli.simpleapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.blibli.simpleapp.dummy.DummyContent.DummyItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class UserAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

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

        holder.tvUserName.text = item.username
        Glide.with(holder.itemView.context)
            .load(item.image)
            .apply(RequestOptions().override(R.dimen.image_user_size, R.dimen.image_user_size))
            .centerCrop()
            .into(holder.ivUserImage)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(users[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: MaterialTextView = view.findViewById(R.id.tv_user_name)
        val ivUserImage: ShapeableImageView = view.findViewById(R.id.iv_user_image)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}