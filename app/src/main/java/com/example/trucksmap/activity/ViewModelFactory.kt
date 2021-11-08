package com.example.trucksmap.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trucksmap.activity.repository.Repository
import com.example.trucksmap.activity.viewmodel.MainViewModel

class ViewModelFactory(private val repo: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}