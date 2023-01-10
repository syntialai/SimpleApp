package com.blibli.simpleapp.feature.user.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @ViewModelInject constructor() : ViewModel() {

    private val _query = MutableLiveData<String?>()
    val query: LiveData<String?>
        get() = _query

    fun onSearchQuerySubmitted(query: String) {
        _query.value = query
    }
}
