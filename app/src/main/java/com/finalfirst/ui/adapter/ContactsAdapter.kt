package com.finalfirst.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finalfirst.data.local.Contact
import com.finalfirst.databinding.ItemContactBinding

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private var contacts = emptyList<Contact>()

    fun submitList(list: List<Contact>) {
        contacts = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.tvName.text = contact.name
            binding.tvPhone.text = contact.phone
        }
    }
}
