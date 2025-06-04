package aps.backflip.curlylab.di

import aps.backflip.curlylab.data.local.preferences.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(): AuthManager {
        return AuthManager
    }
}