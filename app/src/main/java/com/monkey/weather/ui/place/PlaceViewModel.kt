package com.monkey.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.monkey.weather.logic.Repository
import com.monkey.weather.logic.dao.PlaceDao
import com.monkey.weather.logic.model.Place

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) {
        Repository.searchPlaces(it)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(p: Place) = Repository.savePlace(p)
    fun getPlace() = Repository.getPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()

}