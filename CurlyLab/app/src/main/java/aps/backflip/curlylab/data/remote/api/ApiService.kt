package aps.backflip.curlylab.data.remote.api

import aps.backflip.curlylab.data.remote.model.request.auth.LoginRequest
import aps.backflip.curlylab.data.remote.model.request.auth.RefreshTokenRequest
import aps.backflip.curlylab.data.remote.model.request.auth.RegisterRequest
import aps.backflip.curlylab.data.remote.model.request.blog.BlogRecordRequest
import aps.backflip.curlylab.data.remote.model.request.blogsubscriber.BlogSubscriberRequest
import aps.backflip.curlylab.data.remote.model.request.products.FavoriteRequest
import aps.backflip.curlylab.data.remote.model.request.products.ReviewRequest
import aps.backflip.curlylab.data.remote.model.request.profile.HairTypeRequest
import aps.backflip.curlylab.data.remote.model.request.profile.UserRequest
import aps.backflip.curlylab.data.remote.model.response.auth.AuthResponse
import aps.backflip.curlylab.data.remote.model.response.blog.BlogRecordResponse
import aps.backflip.curlylab.data.remote.model.response.blog.BlogSubscriberResponse
import aps.backflip.curlylab.data.remote.model.response.products.ProductResponse
import aps.backflip.curlylab.data.remote.model.response.products.ReviewResponse
import aps.backflip.curlylab.data.remote.model.response.profile.HairTypeResponse
import aps.backflip.curlylab.data.remote.model.response.profile.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface ApiService {
    @GET("blog_records/subscriptions/{id}")
    suspend fun getPostsBySubscribedUsers(@Path("id") id: UUID): List<BlogRecordResponse>

    @GET("blog_records/recommended/{id}")
    suspend fun getRecommendedPostsForUser(@Path("id") id: UUID): List<BlogRecordResponse>

    @GET("blog_records/recommended")
    suspend fun getRecommendedPosts(): List<BlogRecordResponse>

    @GET("blog_records/find/{word}")
    suspend fun findPosts(@Path("word") word: String): List<BlogRecordResponse>

    @GET("blog_records/my/{id}")
    suspend fun findPostsByUser(@Path("id") id: UUID): List<BlogRecordResponse>

    @PUT("blog_records/{id}")
    suspend fun editPost(
        @Path("id") id: UUID,
        @Body blogRecord: BlogRecordRequest
    ): BlogRecordResponse?

    @DELETE("blog_records/{id}")
    suspend fun deletePost(@Path("id") id: UUID): Boolean

    @POST("blog_records/{id}")
    suspend fun addPost(@Body blogRecord: BlogRecordRequest): Boolean


    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    @GET("products/{productId}")
    suspend fun getProductById(@Path("productId") id: UUID): ProductResponse

    @GET("products/favorites/{userId}")
    suspend fun getUserFavorites(@Path("userId") userId: UUID): List<ProductResponse>

    @GET("products/{productId}/is-favorite/{userId}")
    suspend fun isFavorite(
        @Path("productId") productId: UUID,
        @Path("userId") userId: UUID
    ): Boolean

    @POST("products/favorites")
    suspend fun addToFavorites(@Body request: FavoriteRequest)

    @HTTP(method = "DELETE", path = "products/favorites", hasBody = true)
    suspend fun removeFromFavorites(@Body request: FavoriteRequest)

    @GET("products/{id}/reviews")
    suspend fun getReviews(@Path("id") productId: UUID): List<ReviewResponse>

    @POST("products/rate")
    suspend fun addReview(@Body request: ReviewRequest)

    @POST("products/{productId}/reviews/{userId}/update")
    suspend fun updateReview(
        @Path("productId") productId: UUID,
        @Path("userId") userId: UUID,
        @Body request: ReviewRequest
    )

    @DELETE("products/{productId}/reviews/{userId}")
    suspend fun deleteReview(
        @Path("productId") productId: UUID,
        @Path("userId") userId: UUID
    )


    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): AuthResponse

    @POST("/auth/logout")
    suspend fun logout(@Header("Authorization") token: String)


    @POST("/users")
    suspend fun createUser(@Body request: UserRequest): Map<String, String>

    @GET("/users")
    suspend fun getAllUsers(): List<UserResponse>

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") userId: String): UserResponse

    @PUT("/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body request: UserRequest
    )

    @Multipart
    @POST("/users/{id}/uploadImage")
    suspend fun uploadUserImage(
        @Path("id") userId: UUID,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String)


//    @PUT("/hairtypes/{userId}")
//    suspend fun createHairType(
//        @Path("userId") userId: String,
//        @Body request: HairTypeRequest
//    ): Map<String, String>

    @GET("/hairtypes")
    suspend fun getAllHairTypes(): List<HairTypeResponse>

    @GET("/hairtypes/{id}")
    suspend fun getHairType(@Path("id") id: String): HairTypeResponse

    @PUT("/hairtypes/{userId}")
    suspend fun updateHairType(
        @Path("userId") userId: String,
        @Body request: HairTypeRequest
    )

    @DELETE("/hairtypes/{userId}")
    suspend fun deleteHairType(@Path("userId") userId: String)

    @GET("/blog_subscribers/subscriptions/{id}")
    suspend fun subscriptions(@Path("id") id: UUID): Int

    @GET("/blog_subscribers/subscribers/{id}")
    suspend fun subscribers(@Path("id") id: UUID): Int

    @POST("blog_subscribers")
    suspend fun subscribe(@Body blogSubscriberRequest: BlogSubscriberRequest): Boolean

    @DELETE("blog_subscribers/{id}")
    suspend fun unsubscribe(@Path("id") id: UUID): Boolean

    @POST("blog_subscribers/get")
    suspend fun getSubscriptionId(@Body blogSubscriberResponse: BlogSubscriberRequest): BlogSubscriberResponse?
}