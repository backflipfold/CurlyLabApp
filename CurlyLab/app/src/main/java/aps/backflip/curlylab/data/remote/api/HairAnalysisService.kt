package aps.backflip.curlylab.data.remote.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HairAnalysisService {
    @Multipart
    @POST("analyze")
    suspend fun analyzePhoto(
        @Part file: MultipartBody.Part
    ): ResponseBody
}
