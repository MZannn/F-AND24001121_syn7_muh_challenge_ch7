package com.example.movieapplication.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.presentation.ui.adapter.MovieAdapter
import com.example.movieapplication.databinding.FragmentHomeBinding
import com.example.movieapplication.presentation.viewModel.AuthViewModel
import com.example.movieapplication.presentation.viewModel.MovieViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val authViewModel: AuthViewModel by inject()
    private val movieViewModel: MovieViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        lifecycleScope.launch {
            authViewModel.userFlow.collect {
                binding.welcomeUsername.text = "Welcome, ${it?.username}!"
            }
        }

        binding.ibtnProfile.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(action)
        }

        val recyclerView = binding.rvMovies
        movieViewModel.movieResponse.observe(viewLifecycleOwner, Observer {
            val adapter = MovieAdapter(it) { movie ->
                Log.d("HomeFragment", "Movie clicked: ${movie.title}")
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailMovieFragment(movie.id.toString())
                findNavController().navigate(action)
            }
            recyclerView.adapter = adapter
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

}
