package com.hackaprende.restaurantcheck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hackaprende.restaurantcheck.databinding.CheckListFooterBinding
import com.hackaprende.restaurantcheck.databinding.CheckListHeaderBinding
import com.hackaprende.restaurantcheck.databinding.CheckListItemBinding

private const val HEADER_VIEW_TYPE = 1
private const val FOOTER_VIEW_TYPE = 2

class CheckAdapter(private val checkItemList: MutableList<CheckItem>, private val deliveryAddress: String,
                   private val deliveryFee: Double)
    : ListAdapter<CheckItem, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<CheckItem>() {

        override fun areItemsTheSame(oldItem: CheckItem,
                                     newItem: CheckItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CheckItem,
                                        newItem: CheckItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return HEADER_VIEW_TYPE
        } else if (position == checkItemList.size + 1) {
            return FOOTER_VIEW_TYPE
        }

        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        if (viewType == HEADER_VIEW_TYPE) {
            val binding = CheckListHeaderBinding.inflate(LayoutInflater.from(parent.context))
            return HeaderViewHolder(binding)
        }

        if (viewType == FOOTER_VIEW_TYPE) {
            val binding = CheckListFooterBinding.inflate(LayoutInflater.from(parent.context))
            return FooterViewHolder(binding)
        }

        val binding = CheckListItemBinding.inflate(LayoutInflater.from(parent.context))
        return CheckItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        when (holder) {
            is CheckItemViewHolder -> {
                val checkItem = checkItemList[position - 1]
                holder.bind(checkItem)
            }
            is HeaderViewHolder -> holder.bind(deliveryAddress)
            is FooterViewHolder -> holder.bind(getSubtotal(), deliveryFee)
        }
    }

    private fun getSubtotal(): Double {
        var subtotal = 0.0

        for (checkItem in checkItemList) {
            subtotal += (checkItem.price * checkItem.quantity)
        }

        return subtotal
    }

    override fun getItemCount(): Int {
        return checkItemList.size + 2
    }

    inner class FooterViewHolder(binding: CheckListFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val footerSubtotal = binding.footerSubtotal
        private val deliveryFeeText = binding.footerDelivery
        private val totalText = binding.footerTotal
        private val tipText = binding.footerTip
        private val recommendedTipText = binding.recTip
        fun bind(subtotal: Double, deliveryFee: Double) {
            val context = footerSubtotal.context
            recommendedTipText.text = context.getString(R.string.recommended_tip_format,
                10)
            footerSubtotal.text = context.getString(
                R.string.double_price_format,
                subtotal
            )
            deliveryFeeText.text = context.getString(
                R.string.double_price_format,
                deliveryFee
            )

            val total = subtotal + deliveryFee

            totalText.text = context.getString(R.string.double_price_format, total)

            val tip = total * 0.1
            tipText.text = context.getString(R.string.double_price_format, tip)
        }
    }

    inner class HeaderViewHolder(binding: CheckListHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val deliveryAddressText = binding.deliveryAddress

        fun bind(deliveryAddress: String) {
            deliveryAddressText.text = deliveryAddress
        }
    }

    inner class CheckItemViewHolder(binding: CheckListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        private val itemName = binding.itemName
        private val itemQuantity = binding.itemQuantity
        private val itemPrice = binding.itemPrice

        fun bind(checkItem: CheckItem) {
            itemName.text = checkItem.name
            itemQuantity.text = checkItem.quantity.toString()

            val context = itemPrice.context
            itemPrice.text = context.getString(R.string.double_price_format,
                checkItem.price)
        }
    }
}