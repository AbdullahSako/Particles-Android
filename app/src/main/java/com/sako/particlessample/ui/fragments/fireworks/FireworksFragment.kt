package com.sako.particlessample.ui.fragments.fireworks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sako.particlessample.databinding.FragmentFireworksBinding

class FireworksFragment : Fragment() {

    private var _binding: FragmentFireworksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFireworksBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupListeners()

    }

    private fun setupListeners(){

        binding.fireWorksTextView.setOnClickListener {

            //toggle visibility
            if(binding.fireWorksTextView.alpha == 0f){
                binding.fireWorksTextView.animate().alpha(1f).start()
            }else{
                binding.fireWorksTextView.animate().alpha(0f).start()
            }

        }

        binding.fireworksSmudgeButton.setOnClickListener {
            binding.fireworksView.explosionSmudge = !binding.fireworksView.explosionSmudge

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}