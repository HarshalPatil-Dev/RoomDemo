package com.sample.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import androidx.room.Update
import com.sample.roomdemo.db.Subscriber
import com.sample.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscribers = repository.subscribers

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
    get() = statusMessage

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {

        if(inputName.value == null){
            statusMessage.value = Event("Please Enter a name" )

        }
        else if (inputEmail.value == null){
            statusMessage.value = Event("Please Enter a Email address" )

        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please Enter a correct Email address" )
        }
        else{
            if(isUpdateOrDelete){
                subscriberToUpdateDelete.name = inputName.value!!
                subscriberToUpdateDelete.email = inputEmail.value!!
                update(subscriberToUpdateDelete)
            }else {

                var name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }


    }

    fun clearAllorDelete() {

        if (isUpdateOrDelete) {
            delete(subscriberToUpdateDelete)
        } else {
            clearAll()
        }

    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        val newRowId = repository.insert(subscriber)
        if(newRowId > -1) {
            statusMessage.value = Event("Subscriber Inserted Successfully Row ID : $newRowId" )
        }
        else{
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRows = repository.update(subscriber)
        if(noOfRows>0)
        {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Subscriber Updated Successfully")
        }else{
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRows = repository.delete(subscriber)
        if(noOfRows > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"

            statusMessage.value = Event("$noOfRows Subscriber Deleted Successfully")
        }
        else{
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun clearAll() = viewModelScope.launch {
        val noOfRows = repository.deleteAll()
        if(noOfRows > 0) {
            statusMessage.value = Event("$noOfRows All Subscribers Deleted Successfully")
        }
        else{
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateDelete = subscriber

        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}