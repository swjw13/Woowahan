package com.test.woowahan.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.woowahan.R
import com.test.woowahan.data.Menus
import com.test.woowahan.databinding.ActivityMainBinding
import com.test.woowahan.recyclerview.main_activity.MainAdapter
import com.test.woowahan.repo.RequestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * API 호출예제
         * 예제는 삭제/수정 가능합니다.
         *
         * 예제코드의 모든 코드는 변경, 수정 가능합니다.
         */
        lifecycleScope.launch {
            val shop_informations = RequestRepository().requestData().data
            binding.shopName.text = shop_informations.shopInfo.shopName
            binding.shopRating.rating =
                shop_informations.shopInfo.statisticsInfo.starAveragePoint.toFloat()
            binding.shopReviewCount.text =
                "${getString(R.string.cur_review_kor)} : ${shop_informations.shopInfo.statisticsInfo.reviewCountLatest}"
            binding.shopPhone.text = shop_informations.shopInfo.shopNumber
            binding.minimumPrice.text =
                shop_informations.shopInfo.orderCondition.minimumOrderAblePrice.toString()
            binding.tipPrice.text = shop_informations.shopInfo.deliveryTipInfo.deliveryTipPrice
            binding.arrivalTime.text = shop_informations.shopInfo.deliveryTimeInfo.deliveryTimeInfo

            binding.mainRecyclerView.apply {
                this.layoutManager = LinearLayoutManager(this@MainActivity)
                this.adapter = MainAdapter(
                    shop_informations.shopMenu.groupMenus[0].menus
                ) { x: Int -> startDetailActivity(x) }
            }

            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(shop_informations.shopInfo.headerImages[0].url)
                val conn = url.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()

                val input = conn.inputStream
                val bitmap = BitmapFactory.decodeStream(input)

                CoroutineScope(Dispatchers.Main).launch {
                    binding.mainCardView.setImageBitmap(bitmap)
                }
            }
        }
    }


    fun startDetailActivity(menuId: Int) {
        startActivity(DetailActivity.getIntent(this@MainActivity, menuId))
    }
}