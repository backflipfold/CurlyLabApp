package aps.backflip.curlylab.data.remote.api

import android.content.Context
import android.net.Uri
import aps.backflip.curlylab.domain.model.composition.AnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object CompositionApi {

    private val service: CompositionService by lazy {
        ApiClient.retrofit.create(CompositionService::class.java)
    }

    suspend fun analyze(context: Context, text: String?, imageUri: Uri?): AnalysisResult {
        return withContext(Dispatchers.IO) {
            val filePart: MultipartBody.Part? = imageUri?.let { uri ->
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(uri)

                val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
                val extension = when (mimeType) {
                    "image/jpeg" -> ".jpg"
                    "image/png" -> ".png"
                    "image/webp" -> ".webp"
                    else -> ".bin" // на всякий случай
                }

                val tempFile = File.createTempFile("upload_", extension, context.cacheDir)

                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val requestFile = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
            }

            val textPart: RequestBody? = text?.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())

            service.analyzeComposition(filePart, textPart)
        }
    }
}
