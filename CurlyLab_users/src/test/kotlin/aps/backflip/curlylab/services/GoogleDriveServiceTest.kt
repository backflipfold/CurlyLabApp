package aps.backflip.curlylab.services

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.Permission
import org.junit.Test
import org.mockito.kotlin.*
import java.io.ByteArrayInputStream
import java.util.*
import kotlin.test.assertEquals

class GoogleDriveServiceTest {

    private val drive = mock<Drive>()
    private val files = mock<Drive.Files>()
    private val createRequest = mock<Drive.Files.Create>()
    private val permissions = mock<Drive.Permissions>()
    private val permissionCreate = mock<Drive.Permissions.Create>()

    private val service = GoogleDriveService(drive)

    @Test
    fun shouldUploadFileAndReturnUrlAndId() {
        val fileId = "123456"
        val userId = UUID.randomUUID()
        val inputStream = ByteArrayInputStream("test".toByteArray())
        val fileName = "image.jpg"

        whenever(drive.files()).thenReturn(files)
        whenever(files.create(any(), any())).thenReturn(createRequest)
        whenever(createRequest.setFields("id")).thenReturn(createRequest)
        whenever(createRequest.execute()).thenReturn(File().apply { id = fileId })

        whenever(drive.permissions()).thenReturn(permissions)
        whenever(permissions.create(eq(fileId), any())).thenReturn(permissionCreate)
        whenever(permissionCreate.execute()).thenReturn(Permission())

        val (url, returnedId) = service.uploadUserImage(userId, inputStream, fileName)

        assertEquals("https://drive.google.com/uc?export=view&id=$fileId", url)
        assertEquals(fileId, returnedId)
    }
}
