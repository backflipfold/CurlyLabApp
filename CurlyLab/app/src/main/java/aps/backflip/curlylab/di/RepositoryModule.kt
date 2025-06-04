package aps.backflip.curlylab.di

import aps.backflip.curlylab.domain.repository.auth.AuthRepository
import aps.backflip.curlylab.data.repository.auth.AuthRepositoryImpl
import aps.backflip.curlylab.domain.repository.blog.BlogRecordRepository
import aps.backflip.curlylab.data.repository.blog.BlogRecordRepositoryImpl
import aps.backflip.curlylab.data.repository.blogsubscriber.BlogSubscriberRepositoryImpl
import aps.backflip.curlylab.domain.repository.products.ProductsRepository
import aps.backflip.curlylab.data.repository.products.ProductsRepositoryImpl
import aps.backflip.curlylab.domain.repository.profile.HairTypesRepository
import aps.backflip.curlylab.data.repository.profile.HairTypesRepositoryImpl
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import aps.backflip.curlylab.data.repository.profile.UsersRepositoryImpl
import aps.backflip.curlylab.domain.repository.blogsubscriber.BlogSubscriberRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductsRepository(
        impl: ProductsRepositoryImpl
    ): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindBlogRecordRepository(
        impl: BlogRecordRepositoryImpl
    ): BlogRecordRepository

    @Binds
    @Singleton
    abstract fun bindHairTypesRepository(
        impl: HairTypesRepositoryImpl
    ): HairTypesRepository

    @Binds
    @Singleton
    abstract fun bindUsersRepository(
        impl: UsersRepositoryImpl
    ): UsersRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBlogSubscriberRepository(
        impl: BlogSubscriberRepositoryImpl
    ): BlogSubscriberRepository
}