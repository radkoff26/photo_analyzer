package com.example.feature_images_list.api

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
import com.example.core_database.entities.ObjectOnImageType
import com.example.core_navigation.findNavigationController
import com.example.feature_images_list.R
import com.example.feature_images_list.api.ImagesListFragment.Contraction.OBJECT_TYPE
import com.example.feature_images_list.databinding.FragmentImagesListBinding
import com.example.feature_images_list.internal.adapter.ImagesListRecyclerViewAdapter
import com.example.feature_images_list.internal.di.DaggerImagesListFragmentComponent
import com.example.feature_images_list.internal.view_model.ImagesListFragmentViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImagesListFragment : Fragment() {
    private var _binding: FragmentImagesListBinding? = null
    private val binding: FragmentImagesListBinding
        get() = _binding!!

    private var _imagesListAdapter: ImagesListRecyclerViewAdapter? = null
    private val imagesListAdapter: ImagesListRecyclerViewAdapter
        get() = _imagesListAdapter!!

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var imagesListFragmentViewModel: ImagesListFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (arguments == null || arguments?.getString(OBJECT_TYPE) == null) {
            findNavigationController().back()
        } else {
            val typeString = requireArguments().getString(OBJECT_TYPE)!!
            val type = ObjectOnImageType.valueOf(typeString)

            DaggerImagesListFragmentComponent.factory().create(
                requireContext(),
                type,
                requireActivity().application.getBaseDependencies()
            ).inject(this)

            imagesListFragmentViewModel =
                ViewModelProvider(this, viewModelFactory)[ImagesListFragmentViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesListBinding.bind(
            inflater.inflate(R.layout.fragment_images_list, container, false)
        )

        _imagesListAdapter = ImagesListRecyclerViewAdapter(
            imagesListFragmentViewModel::loadImage
        ) {
            findNavigationController().goToImage(it, imagesListFragmentViewModel.type.toString())
        }

        binding.photosRecyclerView.adapter = imagesListAdapter

        loadImages()

        return binding.root
    }

    override fun onStop() {
        imagesListAdapter.cancelScope()
        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        _imagesListAdapter = null
        super.onDestroyView()
    }

    private fun loadImages() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                imagesListAdapter.refreshScope()
                imagesListFragmentViewModel.items.collect {
                    imagesListAdapter.submitData(it)
                }
            }
        }
    }

    object Contraction {
        const val OBJECT_TYPE = "OBJECT_TYPE"
    }
}