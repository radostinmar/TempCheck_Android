package com.rmarinov.tempcheck.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rmarinov.tempcheck.databinding.ItemAlertBinding
import com.rmarinov.tempcheck.ui.model.AlertUiModel

class AlertsAdapter(
    private val onDeleteClick: (id: Int) -> Unit,
    private val onSwitchToggled: (id: Int, isActive: Boolean) -> Unit
) : ListAdapter<AlertUiModel, AlertsAdapter.AlertViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder =
        AlertViewHolder(
            ItemAlertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDeleteClick,
            onSwitchToggled
        )

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int): Unit =
        holder.bind(getItem(position))

    override fun getItemCount(): Int = currentList.size

    private object DiffCallback : DiffUtil.ItemCallback<AlertUiModel>() {

        override fun areItemsTheSame(oldItem: AlertUiModel, newItem: AlertUiModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AlertUiModel, newItem: AlertUiModel): Boolean =
            oldItem == newItem
    }

    class AlertViewHolder(
        private val binding: ItemAlertBinding,
        private val onDeleteClick: (id: Int) -> Unit,
        private val onSwitchToggled: (id: Int, isActive: Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(alertUiModel: AlertUiModel) {
            binding.apply {
                tvAlertDescription.text = alertUiModel.text
                switchActive.isChecked = alertUiModel.isActive
                ivDelete.setOnClickListener { onDeleteClick(alertUiModel.id) }
                switchActive.setOnCheckedChangeListener { _, isChecked -> onSwitchToggled(alertUiModel.id, isChecked) }
            }
        }
    }
}
