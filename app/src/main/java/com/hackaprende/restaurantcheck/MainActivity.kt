package com.hackaprende.restaurantcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackaprende.restaurantcheck.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.checkRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        val checkItemList = mutableListOf<CheckItem>()
        checkItemList.add(CheckItem(1, "Hamburguesa con papas", 2, 80.00))
        checkItemList.add(CheckItem(2, "Agua de jamaica", 2, 20.00))
        checkItemList.add(CheckItem(3, "Rebanada de flan casero", 1, 30.00))
        checkItemList.add(CheckItem(4, "Pizza sin piña", 1, 150.00))
        checkItemList.add(CheckItem(5, "Cerveza", 8, 25.00))

        val checkAdapter = CheckAdapter(checkItemList, "Diagon Alley, London", 40.0)
        recycler.adapter = checkAdapter
    }
}