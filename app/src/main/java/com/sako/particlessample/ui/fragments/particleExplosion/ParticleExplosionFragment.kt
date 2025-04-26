package com.sako.particlessample.ui.fragments.particleExplosion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sako.particlessample.databinding.FragmentParticleExplosionBinding

class ParticleExplosionFragment : Fragment() {

    private var _binding: FragmentParticleExplosionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParticleExplosionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners(){

        binding.partExpFragmentWideBtn.setOnClickListener {
            binding.partExpWideView.startExplosion()
        }

        binding.partExpFragmentFullScreenBtn.setOnClickListener {
            binding.partExpFullScreenView.startExplosion()
        }

        binding.partExpFragmentNarrowBtn.setOnClickListener {
            binding.partExpNarrowView.startExplosion()
        }

        binding.partExpColorfulBtn.setOnClickListener {
            binding.partExpColorfulView.startExplosion()
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}