package com.sako.particlessample.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sako.particlessample.R
import com.sako.particlessample.databinding.FragmentHomeBinding
import com.sako.particlessample.enums.RandomViewEnum

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        showRandomView()

    }

    private fun setupListeners() {

        binding.sparklesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sparklesMainFragment)
        }

        binding.particleExplosionBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_particleExplosionFragment)
        }

        binding.fireworksBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fireworksFragment)
        }

        binding.snowfallBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_snowfallFragment)
        }

        binding.spaceTravelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_spaceTravelFragment)
        }

    }


    private fun showRandomView() {
        val randomViewEnum = RandomViewEnum.entries.random()

        when (randomViewEnum) {
            RandomViewEnum.SPARKLES -> {
                binding.homeSparklesView.visibility = View.VISIBLE
            }

            RandomViewEnum.FIREWORKS -> {
                binding.homeFireworksView.visibility = View.VISIBLE
            }

            RandomViewEnum.SNOWFALL -> {
                binding.homeSnowfallView.visibility = View.VISIBLE
            }

            RandomViewEnum.SPACE_TRAVEL -> {
                binding.homeSpaceTravelView.startAnimation()
                binding.homeSpaceTravelView.visibility = View.VISIBLE
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}