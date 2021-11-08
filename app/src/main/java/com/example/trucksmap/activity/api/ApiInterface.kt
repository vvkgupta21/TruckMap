package com.example.trucksmap.activity.api

import com.example.trucksmap.activity.model.LastRunningState
import com.example.trucksmap.activity.model.TrucksMapResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//const val API_KEY = "4f8b602cd4d640b6b83f307d4aedd7d4"
const val BASE_URL = "https://api.mystral.in/"

//https://api.mystral.in/tt/mobile/logistics/searchTrucks?
// auth-company=PCH&companyId=33&deactivated=false&key=g2qb5jvucg7j8skpu5q7ria0mu&q-expand=true&q-include=lastRunningState,lastWaypoint

interface ApiInterface {

    @GET("tt/mobile/logistics/searchTrucks")
    fun getTrucksData(@Query("auth-company") authCompany: String,
                      @Query("companyId") companyId: Int,
                      @Query("deactivated") deactivated: Boolean,
                      @Query("key") key: String,
                      @Query("q-expand") qExpand: Boolean,
                      @Query("q-include") qInclude : String
    ): Deferred<TrucksMapResponse>

}

//Retrofit Object,
//

object TrucksDataService{
    var truckInstance :ApiInterface
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        truckInstance = retrofit.create(ApiInterface::class.java)
    }
}