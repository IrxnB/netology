package com.example.netology

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class StartViewModel : ViewModel() {
    val size = MutableStateFlow(1.0f)
    val speed = MutableStateFlow(1.0f)
    val count = MutableStateFlow(3)

}