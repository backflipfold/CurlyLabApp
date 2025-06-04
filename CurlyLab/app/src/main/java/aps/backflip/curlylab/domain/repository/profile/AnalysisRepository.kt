// AnalysisRepository.kt
package aps.backflip.curlylab.domain.repository.profile

import aps.backflip.curlylab.data.remote.api.HairAnalysisService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

class AnalysisRepository @Inject constructor(
    private val service: HairAnalysisService
) {
    suspend fun analyzePhoto(imageBytes: ByteArray): String {
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageBytes)
        val part = MultipartBody.Part.createFormData("file", "photo.jpg", requestBody)

        val response = service.analyzePhoto(part)
        val json = response.string()
        val jsonObject = JSONObject(json)
        return jsonObject.optString("porosity", "unknown")
    }
}
