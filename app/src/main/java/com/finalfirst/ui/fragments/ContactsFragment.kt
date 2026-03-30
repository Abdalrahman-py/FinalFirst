package com.finalfirst.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalfirst.databinding.FragmentContactsBinding
import com.finalfirst.ui.adapter.ContactsAdapter
import com.finalfirst.viewmodel.ContactViewModel

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by activityViewModels()
    private val adapter = ContactsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            adapter.submitList(contacts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
