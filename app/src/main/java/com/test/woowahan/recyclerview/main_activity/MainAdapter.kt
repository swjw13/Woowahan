package com.test.woowahan.recyclerview.main_activity

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.woowahan.data.Menus
import com.test.woowahan.databinding.MainMenuItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainAdapter(private var datas: List<Menus>, var change_activity: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainViewHolder(
            MainMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MainViewHolder).binding
        binding.mainMenuItemName.text = datas[position].name
        binding.mainMenuItemDesc.text = datas[position].description
        binding.mainMenuItemPrice.text = datas[position].menuPrices[0].price

        binding.mainMenuItem.setOnClickListener {
            change_activity(datas[position].menuId)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL(datas[position].images?.get(0)!!.url)
            val conn = url.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()

            val input = conn.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            CoroutineScope(Dispatchers.Main).launch {
                binding.mainMenuItemPic.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}