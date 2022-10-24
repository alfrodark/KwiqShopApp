package com.alfrosoft.kwiqshopapp.activities.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfrosoft.kwiqshopapp.R
import com.alfrosoft.kwiqshopapp.databinding.ItemDashboardLayoutBinding
import com.alfrosoft.kwiqshopapp.model.Product
import com.alfrosoft.kwiqshopapp.utils.GlideLoader


class DashboardItemsListAdapter (
    private val context: Context,
    private val list : ArrayList<Product>
        ) : RecyclerView.Adapter<DashboardItemsListAdapter.ViewHolder>() {

    private var onClickListener : OnClickListener? = null

    class ViewHolder(val binding : ItemDashboardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false)
        return ViewHolder(ItemDashboardLayoutBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image,holder.binding.ivDashboardItemImage)
        holder.binding.tvDashboardItemTitle.text = model.title
        holder.binding.tvDashboardItemPrice.text = "$ ${model.price}"

        holder.itemView.setOnClickListener{
            if (onClickListener != null){
                onClickListener!!.onClick(position,model)
            }
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
       return list.size
    }
    interface OnClickListener{
        fun onClick(position: Int,product: Product)

    }
}