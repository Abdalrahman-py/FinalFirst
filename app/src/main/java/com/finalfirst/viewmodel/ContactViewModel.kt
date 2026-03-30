package com.finalfirst.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.finalfirst.contacts.ContactsImporter
import com.finalfirst.data.local.AppDatabase
import com.finalfirst.data.local.Contact
import com.finalfirst.data.repository.ContactRepository
import com.finalfirst.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    private val notificationHelper: NotificationHelper

    val allContacts: LiveData<List<Contact>>

    // One-off event: true = inserted, false = duplicate
    private val _insertResult = MutableSharedFlow<Boolean>(replay = 0)
    val insertResult: SharedFlow<Boolean> = _insertResult.asSharedFlow()

    init {
        val dao = AppDatabase.getInstance(application).contactDao()
        repository = ContactRepository(dao)
        notificationHelper = NotificationHelper(application)
        allContacts = repository.allContacts
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            val rowId = repository.insertContact(contact)
            val inserted = rowId != -1L
            if (inserted) {
                notificationHelper.sendNewContactNotification()
            }
            _insertResult.emit(inserted)
        }
    }

    fun importFromPhone(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val contacts = ContactsImporter(context).getPhoneContacts()
            repository.insertContacts(contacts)
        }
    }
}
