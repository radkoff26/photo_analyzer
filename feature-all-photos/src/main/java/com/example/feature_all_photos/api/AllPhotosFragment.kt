package com.example.feature_all_photos.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.core_images_provider.DeviceImagesProvider
import com.example.feature_all_photos.R
import com.example.feature_all_photos.databinding.FragmentAllPhotosBinding
import com.example.feature_all_photos.internal.AllPhotosRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllPhotosFragment : Fragment() {
    private var _binding: FragmentAllPhotosBinding? = null
    private val binding: FragmentAllPhotosBinding
        get() = _binding!!

    private var _deviceImagesProvider: DeviceImagesProvider? = null
    private val deviceImagesProvider: DeviceImagesProvider
        get() = _deviceImagesProvider!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_all_photos, container, false)

        _binding = FragmentAllPhotosBinding.bind(rootView)
        _deviceImagesProvider = DeviceImagesProvider(requireContext())

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        fetchImagesFromStorage()
    }

    private fun fetchImagesFromStorage() {
        lifecycleScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val bitmapList = deviceImagesProvider.getNextNPhotos(50)
                if (bitmapList != null) {
                    requireActivity().runOnUiThread {
                        binding.photosRecyclerView.adapter = AllPhotosRecyclerViewAdapter(bitmapList)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "AllPhotosFragmentClass"
    }
}