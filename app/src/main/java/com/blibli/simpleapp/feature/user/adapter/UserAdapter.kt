package com.blibli.simpleapp.feature.user.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.blibli.simpleapp.R
import com.blibli.simpleapp.core.util.ImageHelper
import com.blibli.simpleapp.feature.user.model.User
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class UserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var users: ArrayList<User> = arrayListOf()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickedCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            populateItemRows(holder, position)
            return
        }

        showLoadingView(holder as LoadingViewHolder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LOADING) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_progress_bar, parent, false)
            return LoadingViewHolder(view)
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fragment_user_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun getItemViewType(position: Int): Int = if (users[position].login == null) {
        VIEW_TYPE_LOADING
    } else {
        VIEW_TYPE_ITEM
    }

    fun updateList(usersList: ArrayList<User>) {
        users = usersList
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: MaterialTextView = view.findViewById(R.id.tv_user_name)
        val ivUserImage: ShapeableImageView = view.findViewById(R.id.iv_user_image)
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar_circular)
    }

    private fun showLoadingView(holder: LoadingViewHolder, position: Int) {}

    private fun populateItemRows(holder: ItemViewHolder, position: Int) {
        val item = users[position]

        holder.tvUserName.text = item.login
        item.avatar_url?.let { avatar ->
            ImageHelper.resizeAndBuildImage(
                holder.itemView.context,
                avatar,
                holder.ivUserImage,
                R.dimen.image_user_size
            )
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(users[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}