package com.finalfirst.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.finalfirst.data.local.Contact
import com.finalfirst.databinding.FragmentAddContactBinding
import com.finalfirst.viewmodel.ContactViewModel
import kotlinx.coroutines.launch

class AddContactFragment : Fragment() {

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe one-off insert result events
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.insertResult.collect { inserted ->
                    if (inserted) {
                        Toast.makeText(requireContext(), "تم إضافة جهة الاتصال", Toast.LENGTH_SHORT).show()
                        binding.etName.text?.clear()
                        binding.etPhone.text?.clear()
                    } else {
                        Toast.makeText(requireContext(), "الرقم موجود مسبقاً", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "يرجى ملء جميع الحقول", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.insertContact(Contact(name = name, phone = phone))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
