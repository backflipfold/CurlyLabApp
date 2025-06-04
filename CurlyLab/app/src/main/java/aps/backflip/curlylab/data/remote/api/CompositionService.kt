package aps.backflip.curlylab.data.remote.api

import aps.backflip.curlylab.domain.model.composition.AnalysisResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CompositionService {

    @Multipart
    @POST("analyze")
    suspend fun analyzeComposition(
        @Part file: MultipartBody.Part?,       // Файл может быть null
        @Part("text") text: RequestBody?        // Текст может быть null
    ): AnalysisResult
}
