package com.finalfirst.data.repository

import androidx.lifecycle.LiveData
import com.finalfirst.data.local.Contact
import com.finalfirst.data.local.ContactDao

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    suspend fun insertContact(contact: Contact): Long {
        return contactDao.insertContact(contact)
    }
}
