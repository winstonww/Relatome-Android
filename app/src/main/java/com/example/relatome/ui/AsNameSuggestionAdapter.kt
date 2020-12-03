package com.example.relatome.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.databinding.AsnameItemRecyclerBinding
import com.example.relatome.domain.AsNameDomainAsNameSuggestion
import com.example.relatome.viewmodel.AsNameViewModel
import timber.log.Timber


class AsNameSuggestionAdapter(val viewModel: AsNameViewModel) : RecyclerView.Adapter<AsNameSuggestionAdapter.ViewHolder>() {
    var data = listOf<AsNameDomainAsNameSuggestion>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: AsnameItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(AsnameItemRecyclerBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        Timber.i("Here in bindviewholder1")
        holder.binding.asName.text = item.asName
        if (viewModel.selectedPosition == position) {
            holder.binding.divider.visibility = View.VISIBLE
        } else {
            holder.binding.divider.visibility = View.GONE
        }
        holder.binding.root.setOnClickListener {
            if (viewModel.selectedPosition == position) {
                viewModel.selectedPosition = -1
                notifyDataSetChanged()
            } else {
                viewModel.selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getSelectedItem(): AsNameDomainAsNameSuggestion? {
        if (viewModel.selectedPosition == -1) {
            return null
        }
        return data[viewModel.selectedPosition]
    }
}