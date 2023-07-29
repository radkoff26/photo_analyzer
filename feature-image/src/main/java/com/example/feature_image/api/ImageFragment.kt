package com.example.feature_image.api

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.getBaseDependencies
import com.example.core_navigation.findNavigationController
import com.example.feature_image.R
import com.example.feature_image.api.ImageFragment.Contraction.IMAGE_ID
import com.example.feature_image.databinding.FragmentImageBinding
import com.example.feature_image.internal.adapter.ObjectsListAdapter
import com.example.feature_image.internal.di.DaggerImageFragmentComponent
import com.example.feature_image.internal.view_model.ImageFragmentViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding: FragmentImageBinding
        get() = _binding!!

    private var _objectsListAdapter: ObjectsListAdapter? = null
    private val objectsListAdapter: ObjectsListAdapter
        get() = _objectsListAdapter!!

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var imageFragmentViewModel: ImageFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerImageFragmentComponent.factory().create(
            requireContext(),
            requireActivity().application.getBaseDependencies()
        ).inject(this)

        imageFragmentViewModel =
            ViewModelProvider(this, viewModelFactory)[ImageFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.bind(
            inflater.inflate(R.layout.fragment_image, container, false)
        )

        if (arguments?.getLong(IMAGE_ID) == null) {
            findNavigationController().back()
        } else {
            val imageId = requireArguments().getLong(IMAGE_ID)
            imageFragmentViewModel.init(imageId)
        }

        imageFragmentViewModel.requestImageLoading()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageFragmentViewModel.imageBitmapLiveData.observe(
            viewLifecycleOwner
        ) {
            binding.image.setImageBitmap(it)

            initAdapterAndOverlay()
        }
    }

    override fun onDestroyView() {
        _binding = null
        _objectsListAdapter = null
        super.onDestroyView()
    }

    private fun initAdapterAndOverlay() {
        lifecycleScope.launch {
            val objects = imageFragmentViewModel.getObjectsKeySorted()
            _objectsListAdapter = ObjectsListAdapter(
                objects.entries.map { Pair(it.key, it.value) },
                {

                },
                {
                    binding.overlay.spotObject(it)
                }
            )
            binding.objects.adapter = objectsListAdapter

            val image = imageFragmentViewModel.getLoadedImage()!!
            binding.overlay.setResults(
                objects,
                image.height,
                image.width
            )
        }
    }

    object Contraction {
        const val IMAGE_ID = "IMAGE_ID"
    }
}
