package com.finalfirst.contacts

import android.content.Context
import android.provider.ContactsContract
import com.finalfirst.data.local.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsImporter(private val context: Context) {

    suspend fun getPhoneContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (nameIdx == -1 || phoneIdx == -1) return@use
            while (it.moveToNext()) {
                val name = it.getString(nameIdx) ?: continue
                val phone = it.getString(phoneIdx) ?: continue
                contacts.add(Contact(name = name, phone = phone))
            }
        }
        contacts
    }
}
