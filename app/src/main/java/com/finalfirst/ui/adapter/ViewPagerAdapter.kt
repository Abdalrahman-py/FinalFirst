package com.finalfirst.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finalfirst.ui.fragments.AddContactFragment
import com.finalfirst.ui.fragments.ContactsFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ContactsFragment()
        1 -> AddContactFragment()
        else -> ContactsFragment()
    }
}
