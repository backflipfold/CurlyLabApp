package aps.backflip.curlylab.config

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials

object DriveConfig {
    fun createDriveService(): Drive {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jacksonFactory = JacksonFactory.getDefaultInstance()

        val credentialsStream =
            javaClass.classLoader.getResourceAsStream("curlylab-459815-b20c61c333fe.json")

        val credentials = GoogleCredentials.fromStream(credentialsStream)
            .createScoped(
                listOf(
                    "https://www.googleapis.com/auth/drive.appdata",
                    "https://www.googleapis.com/auth/drive.file"
                )
            )

        return Drive.Builder(httpTransport, jacksonFactory, HttpCredentialsAdapter(credentials))
            .setApplicationName("CurlyLab")
            .build()
    }
}