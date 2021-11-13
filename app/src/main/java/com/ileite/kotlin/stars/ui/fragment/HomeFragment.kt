package com.ileite.kotlin.stars.ui.fragment

import androidx.fragment.app.Fragment
import com.ileite.kotlin.stars.R
import com.ileite.kotlin.stars.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}