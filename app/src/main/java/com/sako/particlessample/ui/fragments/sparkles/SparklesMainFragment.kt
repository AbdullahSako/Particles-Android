package com.sako.particlessample.ui.fragments.sparkles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sako.particlessample.R
import com.sako.particlessample.databinding.FragmentSparklesMainBinding

class SparklesMainFragment:Fragment() {

    private var _binding: FragmentSparklesMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSparklesMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners(){

        binding.sparklesMainNextFab.setOnClickListener {
            findNavController().navigate(R.id.action_sparklesMainFragment_to_sparklesButtonFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}