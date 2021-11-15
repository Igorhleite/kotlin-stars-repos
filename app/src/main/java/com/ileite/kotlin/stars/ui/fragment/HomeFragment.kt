package com.ileite.kotlin.stars.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
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
            setOpenRepoUrlInBrowser(repoUrl = repository.repoUrl)
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
        setTryAgainButtonAction()
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
            addLoadStateAdapter()
        }
    }

    private fun addLoadStateAdapter() {
        repositoriesAdapter.addLoadStateListener {
            it.loadStatesRules()
        }
    }

    private fun CombinedLoadStates.loadStatesRules() {
        binding.apply {
            val isLoading = refresh is LoadState.Loading
            val isError = refresh is LoadState.Error

            screenError.root.isVisible =
                (isError) && repositoriesAdapter.itemCount == 0
            pbProgress.isVisible = isLoading
        }
    }

    private fun setTryAgainButtonAction() {
        binding.screenError.btErrorRefresh.setOnClickListener {
            viewModel.getRepositoriesRemotely()
        }
    }

    private fun setOpenRepoUrlInBrowser(repoUrl: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repoUrl)))
    }
}