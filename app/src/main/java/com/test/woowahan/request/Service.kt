package com.test.woowahan.request

import com.test.woowahan.data.Menus
import com.test.woowahan.data.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("2022_wooah_app")
    suspend fun requestData(): ResponseBody


}