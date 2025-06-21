package com.sako.particlessample.ui.fragments.sparkles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sako.particlessample.R
import com.sako.particlessample.databinding.FragmentSparklesSampleViews2Binding

class SparklesSampleViews2Fragment:Fragment() {

    private var _binding: FragmentSparklesSampleViews2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSparklesSampleViews2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners(){

        binding.sparklesSampleViews2PrevFab.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sparklesSampleViews2NextFab.setOnClickListener {
            findNavController().navigate(R.id.action_sparklesSampleViews2Fragment_to_sparklesConnectFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}