package com.sako.particlessample.ui.fragments.sparkles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sako.particlessample.databinding.FragmentSparklesSampleViewsBinding

class SparklesSampleViewsFragment : Fragment() {

    private var _binding: FragmentSparklesSampleViewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSparklesSampleViewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupListeners()
    }

    private fun setupListeners() {

        binding.sparklesSampleViewsPrevFab.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSparkleView.setOnClickListener {
            Log.d("TESTLOG","CLICKED!")
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}