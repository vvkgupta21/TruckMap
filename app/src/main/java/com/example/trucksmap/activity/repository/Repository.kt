package com.example.trucksmap.activity.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trucksmap.activity.api.TrucksDataService
import com.example.trucksmap.activity.model.LastRunningState
import com.example.trucksmap.activity.model.TrucksMapResponse

class Repository(private val trucksService:TrucksDataService) {

    suspend fun getTrucksData(authCompany: String, companyId: Int, deactivated: Boolean, key: String, qExpand: Boolean, qInclude : String): LiveData<TrucksMapResponse> {
        val data = MutableLiveData<TrucksMapResponse>()
        val response = trucksService.truckInstance.getTrucksData(
            authCompany,
            companyId,
            deactivated,
            key,
            qExpand,
            qInclude
        ).await()
        data.value = response
        return data
    }
}