package com.dev.gka.abda.screens.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.gka.abda.utilities.ApiStatus
import com.dev.gka.abda.utilities.Constants
import com.dev.gka.abda.model.TV
import com.dev.gka.abda.model.TvResult
import com.dev.gka.abda.network.MyApi
import kotlinx.coroutines.launch
import timber.log.Timber

class SeriesViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> get() = _status

    private val _airingToday = MutableLiveData<List<TvResult>>()
    val airingToday: LiveData<List<TvResult>> get() = _airingToday

    private val _popular = MutableLiveData<List<TvResult>>()
    val popular: LiveData<List<TvResult>> get() = _popular

    private val _topRated = MutableLiveData<List<TvResult>>()
    val topRated: LiveData<List<TvResult>> get() = _topRated


    // Navigation
    private val _navigateToSelectedSeries = MutableLiveData<TvResult>()
    val navigateToSelectedSeries: LiveData<TvResult>
        get() = _navigateToSelectedSeries

    init {
        airingToday()
        popularSeries()
        topRated()
    }


    private fun airingToday() {
        var airing: MutableList<TV>
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                airing = mutableListOf(
                    MyApi.retrofitService.airingTodaySeries(
                        Constants.AIRING_TODAY,
                        Constants.API_KEY,
                        1
                    )
                )
                for (air in airing) {
                    _airingToday.value = air.results
                }
                _status.value = ApiStatus.DONE
                Timber.d("Airing Size: ${airing.size}")
            } catch (e: Exception) {
                _airingToday.value = ArrayList()
                _status.value = ApiStatus.ERROR
                Timber.w("Failure: ${e.message}")
            }
        }
    }

    private fun popularSeries() {
        var popularSeries: MutableList<TV>
        viewModelScope.launch {
            try {
                popularSeries = mutableListOf(
                    MyApi.retrofitService.getPopularSeries(
                        Constants.POPULAR_PATH,
                        Constants.API_KEY,
                        Constants.PAGES
                    )
                )
                for (popular in popularSeries) {
                    _popular.value = popular.results
                }
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _popular.value = ArrayList()
                _status.value = ApiStatus.ERROR
                Timber.w("Failure: ${e.message}")
            }
        }
    }

    private fun topRated() {
        var topRated: MutableList<TV>
        viewModelScope.launch {
            try {
                topRated = mutableListOf(
                    MyApi.retrofitService.getTopRatedSeries(
                        Constants.TOP_RATED_PATH,
                        Constants.API_KEY,
                        Constants.PAGES
                    )
                )
                for (top in topRated) {
                    _topRated.value = top.results
                }
                _status.value = ApiStatus.DONE
                Timber.d("Top Rated Size: ${topRated.size}")
            } catch (e: Exception) {
                _topRated.value = ArrayList()
                _status.value = ApiStatus.ERROR
                Timber.w("Failure: ${e.message}")
            }
        }
    }

    fun displayMovieDetails(tvProperty: TvResult) {
        _navigateToSelectedSeries.value = tvProperty
    }

    fun displayMovieDetailsComplete() {
        _navigateToSelectedSeries.value = null
    }
}