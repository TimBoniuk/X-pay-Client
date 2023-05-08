package com.example.x_pay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Client(
    val dateTime: String,
    val name: String,
    val number: String,
    val status: String
)

class ClientAdapter(private val clients: List<Client>) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    class ClientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTimeTextView: TextView = view.findViewById(R.id.t2)
        val nameTextView: TextView = view.findViewById(R.id.t1)
        val numberTextView: TextView = view.findViewById(R.id.t3)
        val statusTextView: TextView = view.findViewById(R.id.t4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        holder.dateTimeTextView.text = client.dateTime
        holder.nameTextView.text = client.name
        holder.numberTextView.text = client.number
        holder.statusTextView.text = client.status
    }

    override fun getItemCount() = clients.size
}
