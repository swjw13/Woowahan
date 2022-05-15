package com.test.woowahan.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.woowahan.R
import com.test.woowahan.data.Menus
import com.test.woowahan.databinding.ActivityDetailBinding
import com.test.woowahan.recyclerview.main_activity.MainAdapter
import com.test.woowahan.repo.RequestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class DetailActivity: AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding: ActivityDetailBinding get() = requireNotNull(_binding)

    companion object {
        fun getIntent(context: Context, menuId: Int): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("menuId", menuId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("menuId", 0)

        lifecycleScope.launch {
            val shop = RequestRepository().requestData().data
            val all_menus = RequestRepository().requestData().data.shopMenu.groupMenus[0].menus
            var specific_menu: Menus? = null
            for(i in all_menus){
                if (i.menuId == id){
                    specific_menu = i
                    break
                }
            }
            if(specific_menu != null){
                binding.menuSpecificName.text = specific_menu.name
                binding.menuSpecificDesc.text = specific_menu.description
                binding.menuSpecificLeastPrice.text = shop.shopInfo.orderCondition.minimumOrderAblePrice.toString()
            }

            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(specific_menu?.images?.get(0)!!.url)
                val conn = url.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()

                val input = conn.inputStream
                val bitmap = BitmapFactory.decodeStream(input)

                CoroutineScope(Dispatchers.Main).launch {
                    binding.subImageView.setImageBitmap(bitmap)
                }
            }
            for (i in specific_menu?.optionGroups?.get(1)!!.options){
                val checkBox = CheckBox(this@DetailActivity).apply {
                    this.text = i.name
                }
                binding.specificAdditionalLayout.addView(checkBox)
            }
        }

    }

    private fun startConfirmActivity() {
        ConfirmActivity.getIntent(this@DetailActivity)
    }
}