package com.ileite.kotlin.stars.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.ileite.kotlin.stars.R
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.databinding.RepositoryItemBinding

class RemoteRepositoriesAdapter(
    private val onClickListener: (repository: GitRepositoryModel, position: Int) -> Unit
) :
    PagingDataAdapter<GitRepositoryModel, RemoteRepositoriesAdapter.ViewHolder>(
        RemoteRepositoriesAdapter) {

    private companion object : DiffUtil.ItemCallback<GitRepositoryModel>() {
        override fun areItemsTheSame(
            oldItem: GitRepositoryModel,
            newItem: GitRepositoryModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GitRepositoryModel,
            newItem: GitRepositoryModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { repository ->
            holder.initView(repository)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = RepositoryItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(biding, onClickListener)
    }

    inner class ViewHolder(
        private val binding: RepositoryItemBinding,
        private val onClickListener: (repository: GitRepositoryModel, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun initView(repository: GitRepositoryModel) {
            binding.apply {
                tvOwnerName.text = repository.ownerName
                tvRepositoryName.text = repository.name
                tvStarsValue.text = repository.stars
                tvForksValue.text = repository.forks
                ivRepositoryImage.load(repository.ownerPhotoUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_placeholder)
                    transformations(CircleCropTransformation())
                }
                root.setOnClickListener {
                    onClickListener(repository, layoutPosition)
                }
            }
        }
    }
}