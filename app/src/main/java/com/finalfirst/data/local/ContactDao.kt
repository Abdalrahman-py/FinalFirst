package com.finalfirst.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {

    // Returns the new row id, or -1 if the phone number already exists (duplicate)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact): Long

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): LiveData<List<Contact>>
}
