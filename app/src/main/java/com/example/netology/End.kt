package com.example.netology

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.netology.databinding.FragmentEndBinding


class End : Fragment() {
    private lateinit var binding: FragmentEndBinding
    private val args: EndArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEndBinding.inflate(inflater, container, false)
        binding.successValue.text = args.success.toString()
        binding.totalValue.text = args.total.toString()
        binding.returnButton.setOnClickListener {
            var action = EndDirections.actionEndToStart()
            findNavController().navigate(action)
        }
        return binding.root
    }
}