package com.alfrosoft.kwiqshopapp.activities.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfrosoft.kwiqshopapp.R
import com.alfrosoft.kwiqshopapp.activities.AddEditAddressActivity
import com.alfrosoft.kwiqshopapp.activities.CheckoutActivity
import com.alfrosoft.kwiqshopapp.databinding.ItemAddressLayoutBinding
import com.alfrosoft.kwiqshopapp.model.Address
import com.alfrosoft.kwiqshopapp.utils.Constants


class AddressListAdapter (private val context: Context,
                          private val list : ArrayList<Address>, private val mSelectedAddress : Boolean) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    class ViewHolder(val binding : ItemAddressLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address_layout,parent,false)
        return ViewHolder(ItemAddressLayoutBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvAddressFullName.text = model.name
        holder.binding.tvAddressMobileNumber.text = model.mobileNumber
        holder.binding.tvAddressType.text = model.type
        holder.binding.tvAddressDetails.text = "${model.address}, ${model.zipcode}"

        if (mSelectedAddress){
            holder.itemView.setOnClickListener{
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                context.startActivity(intent)
            }
        }
    }

    fun notifyEditItem(activity : Activity,position: Int){
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS,list[position])
        activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}