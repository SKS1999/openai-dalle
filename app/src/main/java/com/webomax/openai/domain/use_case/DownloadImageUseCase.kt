package com.webomax.openai.domain.use_case

import android.app.Application
import com.webomax.openai.common.DownloadImage
import javax.inject.Inject

class DownloadImageUseCase @Inject constructor(private val application: Application) :
    DownloadImage() {
    operator fun invoke(url: String) = downloadImageFromURL(url, application)

}