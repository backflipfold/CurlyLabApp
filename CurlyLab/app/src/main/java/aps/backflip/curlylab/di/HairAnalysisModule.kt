package aps.backflip.curlylab.di

import aps.backflip.curlylab.data.remote.api.HairAnalysisService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HairAnalysisModule {

    private const val BASE_URL = "http://hairtyping-service:8111/"

    @Provides
    @Singleton
    fun provideHairAnalysisService(): HairAnalysisService {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HairAnalysisService::class.java)
    }
}
