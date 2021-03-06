package com.dev.gka.abda.screens.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.gka.abda.model.Result

class DetailsViewModel(result: Result) : ViewModel() {
    private val _selectedMovie = MutableLiveData<Result>()
    val selected: LiveData<Result>
        get() = _selectedMovie

    init {
        _selectedMovie.value = result
    }

}