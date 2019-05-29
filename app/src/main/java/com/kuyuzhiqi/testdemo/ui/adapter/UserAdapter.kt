package com.kuyuzhiqi.testdemo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.model.User

class UserAdapter(private val list: MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<com.kuyuzhiqi.testdemo.databinding.ItemUserBinding>(
                LayoutInflater.from(parent.context), R.layout.item_user, parent, false)
        return UserViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int = list.count()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.viewDataBinding.setVariable(com.kuyuzhiqi.testdemo.BR.user, list[position])
        holder.viewDataBinding.executePendingBindings()
    }

    inner class UserViewHolder(viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        val viewDataBinding = viewDataBinding
    }
}