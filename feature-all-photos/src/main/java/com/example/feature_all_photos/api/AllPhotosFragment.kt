package com.example.feature_all_photos.api

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.base.getBaseDependencies
import com.example.feature_all_photos.R
import com.example.feature_all_photos.databinding.FragmentAllPhotosBinding
import com.example.feature_all_photos.internal.adapter.AllPhotosRecyclerViewAdapter
import com.example.feature_all_photos.internal.data_source.ImagesMediator
import com.example.feature_all_photos.internal.di.DaggerAllPhotosFragmentComponent
import com.example.feature_all_photos.internal.view_model.AllPhotosFragmentViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllPhotosFragment : Fragment() {
    private var _binding: FragmentAllPhotosBinding? = null
    private val binding: FragmentAllPhotosBinding
        get() = _binding!!

    @Inject
    internal lateinit var imagesMediator: ImagesMediator

    private var _recyclerViewAdapter: AllPhotosRecyclerViewAdapter? = null
    private val recyclerViewAdapter: AllPhotosRecyclerViewAdapter
        get() = _recyclerViewAdapter!!

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AllPhotosFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerAllPhotosFragmentComponent.factory().create(
            requireContext(),
            requireActivity().application.getBaseDependencies()
        ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_all_photos, container, false)

        _binding = FragmentAllPhotosBinding.bind(rootView)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[AllPhotosFragmentViewModel::class.java]

        _recyclerViewAdapter = AllPhotosRecyclerViewAdapter(viewModel::loadImageByImageId, 2)

        binding.photosRecyclerView.adapter = recyclerViewAdapter

        fetchImagesFromStorage()

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        recyclerViewAdapter.cancelScope()
    }

    private fun fetchImagesFromStorage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recyclerViewAdapter.refreshScope()
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