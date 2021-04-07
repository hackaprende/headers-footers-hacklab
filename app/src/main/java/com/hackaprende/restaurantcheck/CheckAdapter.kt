package com.hackaprende.restaurantcheck

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackaprende.restaurantcheck.databinding.CheckListFooterBinding
import com.hackaprende.restaurantcheck.databinding.CheckListHeaderBinding
import com.hackaprende.restaurantcheck.databinding.CheckListItemBinding
import java.util.*

class CheckAdapter(private val activity: Activity, private val checkItemList: MutableList<CheckItem>,
                   private val deliveryAddress: String, private val deliveryFee: Double) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER_VIEW = 1
        private const val FOOTER_VIEW = 2
    }

    private var isCheckReview = false
    private var tableTotal = 0.0

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return HEADER_VIEW
        } else if (position == checkItemList.size + 1) {
            return FOOTER_VIEW
        }

        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER_VIEW) {
            val binding = CheckListHeaderBinding.inflate(LayoutInflater.from(parent.context))
            return HeaderViewHolder(binding)
        } else if (viewType == FOOTER_VIEW) {
            val binding = CheckListFooterBinding.inflate(LayoutInflater.from(parent.context))
            return FooterViewHolder(binding)
        }

        val binding = CheckListItemBinding.inflate(LayoutInflater.from(parent.context))
        return CheckItemHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CheckItemHolder -> holder.bindView(position)
            is FooterViewHolder -> holder.bindView()
            is HeaderViewHolder -> holder.bindView()
        }
    }

    override fun getItemCount(): Int {
        if (checkItemList.size == 0) {
            // Header + footer
            return 2
        }

        // Add 2 extra views to show the header and footer views
        return checkItemList.size + 2
    }

    inner class CheckItemHolder(binding: CheckListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val name = binding.checkListItemName
        private val quantity = binding.checkListItemQuantity
        private val price = binding.checkListItemPrice

        fun bindView(position: Int) {
            // We need to insert position -1 because of the footer
            val checkItem = checkItemList[position - 1]

            name.text = checkItem.name

            val checkItemQuantity = checkItem.quantity

            quantity.text = checkItemQuantity.toString()
            price.text = String.format(activity.getString(R.string.double_price_format), checkItem.price)

        }
    }

    inner class HeaderViewHolder(binding: CheckListHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        private val deliveryAddressText = binding.checkListHeaderDeliveryAddressText

        fun bindView() {
            deliveryAddressText.text = deliveryAddress
        }
    }

    inner class FooterViewHolder(binding: CheckListFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        private val subtotalText = binding.checkListFooterSubtotalText
        private val userCheckTotalTextView = binding.checkListFooterTotalText
        private val deliveryFeeText = binding.checkListFooterDeliveryFeeText
        private val recommendedTipText = binding.checkListFooterRecommendedTipText
        private val recommendedTipTitle = binding.checkListFooterRecommendedTipTitle

        fun bindView() {
            var totalPrice = getCheckTotal()

            subtotalText.text = String.format(activity.getString(R.string.double_price_format), totalPrice)

            deliveryFeeText.text = String.format(activity.getString(R.string.double_price_format), deliveryFee)
            totalPrice += deliveryFee

            userCheckTotalTextView.text = String.format(activity.getString(R.string.double_price_format), totalPrice)
            recommendedTipTitle.text = String.format(activity.getString(R.string.recommended_tip_format), 10)
            recommendedTipText.text = String.format(activity.getString(R.string.double_price_format), totalPrice / 10)
        }

        private fun getCheckTotal(): Double {
            var total = 0.0
            for (checkItem in checkItemList) {
                total += (checkItem.quantity * checkItem.price)
            }

            return total
        }
    }
}
