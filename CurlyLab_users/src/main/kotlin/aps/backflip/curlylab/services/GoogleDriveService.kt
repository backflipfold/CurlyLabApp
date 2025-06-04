package aps.backflip.curlylab.services

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import java.io.InputStream
import java.util.UUID

class GoogleDriveService(private val drive: Drive) {

    companion object {
        private val SUPPORTED_MIME_TYPES = mapOf(
            "jpg" to "image/jpeg",
            "jpeg" to "image/jpeg",
            "png" to "image/png",
            "gif" to "image/gif"
        )
    }

    fun uploadUserImage(
        userId: UUID,
        inputStream: InputStream,
        fileName: String
    ): Pair<String, String> {
        val fileExtension = fileName.substringAfterLast(".", "").lowercase()
        val tempFile = kotlin.io.path.createTempFile(prefix = "upload_", suffix = ".$fileExtension")
            .toFile().also { file ->
                try {
                    inputStream.use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                } catch (e: Exception) {
                    throw RuntimeException("Failed to create temp file", e)
                }
            }

        return try {
            val mimeType = SUPPORTED_MIME_TYPES[fileExtension] ?: "application/octet-stream"

            val fileMetadata = File().apply {
                name = "$userId-$fileName"
            }

            val mediaContent = FileContent(mimeType, tempFile)
            val uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute()

            val permission = com.google.api.services.drive.model.Permission().apply {
                type = "anyone"
                role = "reader"
            }
            drive.permissions().create(uploadedFile.id, permission).execute()

            val url = "https://drive.google.com/uc?export=view&id=${uploadedFile.id}"
            url to uploadedFile.id
        } catch (e: Exception) {
            throw RuntimeException("Google Drive upload failed", e)
        } finally {
            tempFile.delete()
        }
    }

    fun deleteFile(fileId: String) {
        try {
            drive.files().get(fileId).execute()
            drive.files().delete(fileId).execute()
            println("File with ID $fileId deleted")
        } catch (e: GoogleJsonResponseException) {
            if (e.statusCode == 404) {
                println("File not found, skipping deletion")
            } else {
                throw e
            }
        }
    }
}