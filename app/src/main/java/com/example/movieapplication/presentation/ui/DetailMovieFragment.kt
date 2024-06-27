package com.example.movieapplication.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapplication.databinding.FragmentDetailMovieBinding
import com.example.movieapplication.presentation.viewModel.DetailMovieViewModel
import com.example.movieapplication.presentation.viewModel.MovieViewModel
import org.koin.android.ext.android.inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DetailMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailMovieFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentDetailMovieBinding
    private val args: DetailMovieFragmentArgs by navArgs()
    private val viewModel: DetailMovieViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        viewModel.movieDetailResponse.observe(viewLifecycleOwner, Observer{
            it?.let {
                binding.tvTitle.text = it.title
                binding.tvOverview.text = it.overview
                Glide.with(binding.root)
                    .load("https://image.tmdb.org/t/p/w500${it.posterPath}")
                    .into(binding.imageDetailMovie)
                binding.imageDetailMovie.background = null
            }
        })
        viewModel.getMovieDetail(args.id)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailMovieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailMovieFragment().apply {
            }
    }
}