package com.example.feature_all_photos.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core_database.ApplicationDatabase
import com.example.feature_all_photos.R
import com.example.feature_all_photos.databinding.FragmentAllPhotosBinding
import com.example.feature_all_photos.internal.adapter.AllPhotosRecyclerViewAdapter
import com.example.feature_all_photos.internal.data_source.ImagesMediator
import com.example.feature_all_photos.internal.view_model.AllPhotosFragmentViewModel
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllPhotosFragment : DaggerFragment() {
    private var _binding: FragmentAllPhotosBinding? = null
    private val binding: FragmentAllPhotosBinding
        get() = _binding!!

    @Inject
    internal lateinit var applicationDatabase: ApplicationDatabase

    @Inject
    internal lateinit var imagesMediator: ImagesMediator

    private var _recyclerViewAdapter: AllPhotosRecyclerViewAdapter? = null
    private val recyclerViewAdapter: AllPhotosRecyclerViewAdapter
        get() = _recyclerViewAdapter!!

    @Inject
    internal lateinit var viewModel: AllPhotosFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_all_photos, container, false)

        _binding = FragmentAllPhotosBinding.bind(rootView)

        viewModel = ViewModelProvider(
            this,
            AllPhotosFragmentViewModel.Factory(
                requireContext(),
                imagesMediator
            )
        )[AllPhotosFragmentViewModel::class.java]

        _recyclerViewAdapter = AllPhotosRecyclerViewAdapter(viewModel::loadImageByImageId, 2)

        binding.photosRecyclerView.adapter = recyclerViewAdapter

        fetchImagesFromStorage()

        return binding.root
    }

    private fun fetchImagesFromStorage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    recyclerViewAdapter.submitData(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "AllPhotosFragmentClass"
    }
}