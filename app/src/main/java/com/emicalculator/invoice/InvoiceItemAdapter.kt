package com.emicalculator.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emicalculator.databinding.InvoiceItemBinding
import kotlin.math.roundToInt

class InvoiceItemAdapter(val list: List<ItemModel>) :
    RecyclerView.Adapter<InvoiceItemAdapter.ItemVH>() {
    var mlist : ArrayList<ItemModel>  = list as ArrayList<ItemModel>

    class ItemVH(val invoiceItemBinding: InvoiceItemBinding) :
        RecyclerView.ViewHolder(invoiceItemBinding.root) {
        fun bindView(model: ItemModel) {
            invoiceItemBinding.itemDescription.text = "${model.itemDescription}"
            invoiceItemBinding.itemPeritem.text = "${model.itemPricePerItem}"
            invoiceItemBinding.itemQty.text = "${model.itemQuantity}"
            invoiceItemBinding.itemTotal.text = "${model.itemFinalAmount}"
            invoiceItemBinding.itemGst.text = "${model.itemRateofGST.toDouble().roundToInt()} %"

        }

    }

    fun addItem(item : ItemModel){
        mlist.add(item)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val binding = InvoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(binding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bindView(mlist[position])
    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}