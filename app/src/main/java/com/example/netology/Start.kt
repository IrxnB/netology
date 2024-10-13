package com.example.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.netology.databinding.ActivityMainBinding
import com.example.netology.databinding.FragmentStartBinding
import com.google.android.material.slider.Slider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Start : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var model: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)

        model = ViewModelProvider(requireActivity())[StartViewModel::class.java]

        binding.sizeBar.value = model.size.value
        binding.speedBar.value = model.speed.value
        binding.mouseCountBar.value = model.count.value.toFloat()

        lifecycleScope.launch {
            model.count.collectToTextView(binding.countValue)
        }
        lifecycleScope.launch {
            model.speed.collectToTextView(binding.speedValue)
        }
        lifecycleScope.launch {
            model.size.collectToTextView(binding.sizeValue)
        }

        binding.speedBar.addNewValueConsumer { model.speed.update { _ -> it } }
        binding.sizeBar.addNewValueConsumer { model.size.update { _ -> it } }
        binding.mouseCountBar.addNewValueConsumer { model.count.update { _ -> it.toInt() } }

        binding.playButton.setOnClickListener {
            val action = StartDirections.actionStartToGame(model.size.value, 10, model.count.value, model.speed.value)
            findNavController().navigate(action)
        }

        binding.statsButton.setOnClickListener {
            val action = StartDirections.actionStartToStats()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun Slider.addNewValueConsumer(consumer: (Float) -> Unit) =
        this.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                consumer.invoke(slider.value)
            }
        })


    private suspend fun <T> Flow<T>.collectToTextView(textView: TextView) = this.collect {
        textView.text = it.toString()
    }

}