package com.learn.codingChallenge.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learn.codingChallenge.R
import com.learn.codingChallenge.models.Transaction
import com.learn.codingChallenge.utils.getDate
import com.learn.codingChallenge.utils.getDollarAmount

class TransactionsAdapter: RecyclerView.Adapter<TransactionsAdapter.TransactionItemVH>() {

    var transactionList: List<Transaction>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class TransactionItemVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val merchantName: TextView = itemView.findViewById(R.id.merchantName)
        val date: TextView = itemView.findViewById(R.id.date)
        val price: TextView = itemView.findViewById(R.id.price)
        val recurring: TextView = itemView.findViewById(R.id.recurring)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent,false)
        return TransactionItemVH(view)
    }

    override fun onBindViewHolder(holder: TransactionItemVH, position: Int) {
        transactionList?.getOrNull(position)?.let { transaction ->
            holder.merchantName.text = transaction.merchantName
            holder.date.text = getDate(transaction.timestamp)
            holder.price.text = getDollarAmount(transaction.amountCents)
            holder.recurring.visibility = if(transaction.isRecurring) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = transactionList?.size ?: 0

}
