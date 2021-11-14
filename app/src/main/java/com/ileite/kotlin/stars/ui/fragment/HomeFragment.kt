package com.ileite.kotlin.stars.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.ileite.kotlin.stars.R
import com.ileite.kotlin.stars.databinding.FragmentHomeBinding
import com.ileite.kotlin.stars.ui.adapter.RemoteRepositoriesAdapter
import com.ileite.kotlin.stars.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    val repositoriesAdapter by lazy {
        RemoteRepositoriesAdapter { repository, _ ->

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        initRemoteDataObserver()
        initRecycler()
    }

    private fun initRecycler() {
        binding.rcRepositories.apply {
            adapter = repositoriesAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            isVerticalFadingEdgeEnabled = true
            setFadingEdgeLength(150)
        }
    }

    private fun initRemoteDataObserver() {
        viewModel.repositoriesEvent.observe(viewLifecycleOwner) {
            repositoriesAdapter.submitData(lifecycle, it)
        }
    }
}