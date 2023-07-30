package com.example.feature_all_photos.api

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.example.core_actions.Actions
import com.example.core_navigation.findNavigationController
import com.example.feature_all_photos.R
import com.example.feature_all_photos.databinding.FragmentAllPhotosBinding
import com.example.feature_all_photos.internal.adapter.AllPhotosRecyclerViewAdapter
import com.example.feature_all_photos.internal.di.DaggerAllPhotosFragmentComponent
import com.example.feature_all_photos.internal.view_model.AllPhotosFragmentViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllPhotosFragment : Fragment() {
    private var _binding: FragmentAllPhotosBinding? = null
    private val binding: FragmentAllPhotosBinding
        get() = _binding!!

    private var _recyclerViewAdapter: AllPhotosRecyclerViewAdapter? = null
    private val recyclerViewAdapter: AllPhotosRecyclerViewAdapter
        get() = _recyclerViewAdapter!!

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AllPhotosFragmentViewModel

    private val databaseUpdateBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
//            recyclerViewAdapter.refresh()
        }
    }

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

        _recyclerViewAdapter = AllPhotosRecyclerViewAdapter(viewModel::loadImageByImageId) {
            findNavigationController().goToImage(it, null)
        }

        binding.photosRecyclerView.adapter = recyclerViewAdapter

        fetchImagesFromStorage()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            databaseUpdateBroadcastReceiver,
            IntentFilter(Actions.DATABASE_UPDATED_ACTION)
        )
    }

    override fun onPause() {
        requireActivity().unregisterReceiver(databaseUpdateBroadcastReceiver)
        super.onPause()
    }

    override fun onStop() {
        recyclerViewAdapter.cancelScope()
        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        _recyclerViewAdapter = null
        super.onDestroyView()
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
}