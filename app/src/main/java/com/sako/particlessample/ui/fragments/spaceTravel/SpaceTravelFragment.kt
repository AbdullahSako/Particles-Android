package com.sako.particlessample.ui.fragments.spaceTravel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sako.particlessample.databinding.FragmentSpaceTravelBinding

class SpaceTravelFragment: Fragment() {

    private var _binding: FragmentSpaceTravelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpaceTravelBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners(){

        binding.spaceTravelStartButton.setOnClickListener {

            if(binding.spaceTravelView.isRunning){
                binding.spaceTravelView.stopAnimation()
                binding.spaceTravelStartButton.text = "Start"
            }else{
                binding.spaceTravelView.startAnimation()
                binding.spaceTravelStartButton.text = "Stop"
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}