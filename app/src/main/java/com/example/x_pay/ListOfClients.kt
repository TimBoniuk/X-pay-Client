package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListOfClients : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)
        val listOfClients = findViewById<RecyclerView>(R.id.listOfCliennts)

        val clients = listOf(
            Client("12/02/23:10", "Penny Cohen", "98654334211", "Active"),
            Client("12/03/23:10", "John Doe", "98654334212", "Active"),
            // Добавьте еще 4 объекта Client
        )
        listOfClients.layoutManager = LinearLayoutManager(this)


        listOfClients.adapter = ClientAdapter(clients)
    }
}
