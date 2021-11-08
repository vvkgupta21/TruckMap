package com.example.trucksmap.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trucksmap.activity.StateLiveData
import com.example.trucksmap.activity.model.LastRunningState
import com.example.trucksmap.activity.model.TrucksMapResponse
import com.example.trucksmap.activity.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val repo:Repository):ViewModel() {

    private lateinit var viewModelJob: Job

    fun getTrucksData(authCompany: String, companyId: Int, deactivated: Boolean, key: String, qExpand: Boolean, qInclude : String): StateLiveData<TrucksMapResponse>{
        val data = StateLiveData<TrucksMapResponse>()
        data.postLoading()
        viewModelJob = CoroutineScope(Job() + Dispatchers.Main).launch {
            try {
                val response = repo.getTrucksData(authCompany, companyId, deactivated, key, qExpand, qInclude)
                response.value?.let { data.postSuccess(it)}
            } catch (e: Exception) {
                data.postError(e)
            }
        }
        return data
    }

    @Suppress("UNCHECKED_CAST")
    class Factory
    constructor(
        private val repo: Repository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java))
                return MainViewModel(
                    repo
                ) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}