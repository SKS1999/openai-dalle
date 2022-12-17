package com.webomax.openai.presentation.generate_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webomax.openai.common.Constants.SIZE_256
import com.webomax.openai.common.Resource
import com.webomax.openai.common.Sizes
import com.webomax.openai.data.model.GeneratedImage
import com.webomax.openai.data.model.RequestBody
import com.webomax.openai.domain.use_case.GenerateImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateImageViewModel @Inject constructor(private val generateImageUseCase: GenerateImageUseCase) :
    ViewModel() {


    private val _state = MutableStateFlow<Resource<GeneratedImage>?>(null)
    val state = _state.asStateFlow()

    fun generateImage(prompt: String, n: Int, size: Sizes) = viewModelScope.launch {


            Sizes.SIZE_256




        generateImageUseCase(RequestBody(n, prompt, SIZE_256)).collect {
            _state.emit(it)
        }
    }


}