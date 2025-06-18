package com.sako.particlessample.ui.fragments.snowfall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sako.particlessample.databinding.FragmentSnowfallBinding

class SnowfallFragment : Fragment() {

    private var _binding: FragmentSnowfallBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSnowfallBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupListeners()

    }

    private fun setupListeners(){

        binding.snowfallTextView.setOnClickListener {
            //toggle visibility
            if(binding.snowfallTextView.alpha == 0f){
                binding.snowfallTextView.animate().alpha(1f).start()
            }else{
                binding.snowfallTextView.animate().alpha(0f).start()
            }
        }

        binding.snowfallColorsButton.setOnClickListener {
            binding.snowFallView.randomColor = !binding.snowFallView.randomColor
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}