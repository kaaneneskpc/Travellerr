package com.kaaneneskpc.data.dataSource


import com.kaaneneskpc.data.model.BookingAvailabilityDto
import com.kaaneneskpc.data.model.BookingDto
import com.kaaneneskpc.data.model.PaymentIntentDto
import com.kaaneneskpc.data.model.TravelListingDto
import com.kaaneneskpc.data.model.request.BookingInfoRequest
import com.kaaneneskpc.data.model.request.PaymentIntentInfoRequest
import com.kaaneneskpc.data.model.request.RegisterRequest
import com.kaaneneskpc.data.model.request.SignInRequest
import com.kaaneneskpc.data.model.response.ListingResponse
import com.kaaneneskpc.data.model.response.SignInResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteDataSource(private val httpClient: HttpClient, private val baseUrl: String, private val cacheDataSource: CacheDataSource) {
    private val BASE_URL = baseUrl
    private val SIGN_IN_ENDPOINT = "${BASE_URL}/auth/login"
    private val REGISTER_ENDPOINT = "${BASE_URL}/auth/register"
    private val LISTING_ENDPOINT = "${BASE_URL}/listings"
    private fun getListingByIDEndpoint(id: String) = "${BASE_URL}/listings/$id"

    suspend fun signIn(request: SignInRequest): Result<SignInResponse> {
        return try {
            val response = httpClient.post(urlString = SIGN_IN_ENDPOINT) {
                setBody(request)
            }
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
    suspend fun register(request: RegisterRequest): Result<SignInResponse> {
        return try {
            val response = httpClient.post(urlString = REGISTER_ENDPOINT) {
                setBody(request)
            }
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getAllListing() : Result<ListingResponse>{
        return try {
            val response = httpClient.get(urlString = LISTING_ENDPOINT)
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getListingByID(id: String) : Result<TravelListingDto>{
        return try {
            val response = httpClient.get(urlString = getListingByIDEndpoint(id))
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
    suspend fun checkBookingAvailability(request: BookingInfoRequest) : Result<BookingAvailabilityDto>{
        return try {
            val token = cacheDataSource.getAuthToken()
            println("DEBUG_BOOKING: ===== CHECK AVAILABILITY START =====")
            println("DEBUG_BOOKING: Token is null? ${token == null}")
            println("DEBUG_BOOKING: Token value='${token ?: "NULL"}'")
            println("DEBUG_BOOKING: URL=${BASE_URL}/bookings/check-availability")
            println("DEBUG_BOOKING: Request body=$request")
            println("DEBUG_BOOKING: Auth header=Bearer $token")
            val response = httpClient.post(urlString = "${BASE_URL}/bookings/check-availability") {
                setBody(request)
                header("Authorization", "Bearer $token")
            }
            println("DEBUG_BOOKING: Response status=${response.status}")
            val body: BookingAvailabilityDto = response.body()
            println("DEBUG_BOOKING: Parsed body=$body")
            println("DEBUG_BOOKING: ===== CHECK AVAILABILITY SUCCESS =====")
            Result.success(body)
        } catch (ex: Exception) {
            println("DEBUG_BOOKING: ===== CHECK AVAILABILITY FAILED =====")
            println("DEBUG_BOOKING: Exception class=${ex::class.simpleName}")
            println("DEBUG_BOOKING: Exception message=${ex.message}")
            println("DEBUG_BOOKING: Full exception=$ex")
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
    suspend fun createBooking(request: BookingInfoRequest) : Result<BookingDto>{
        return try {
            val token = cacheDataSource.getAuthToken()
            println("DEBUG_CREATE_BOOKING: Token='${token ?: "NULL"}', Request=$request")
            val response = httpClient.post(urlString = "${BASE_URL}/bookings") {
                setBody(request)
                header("Authorization", "Bearer $token")
            }
            println("DEBUG_CREATE_BOOKING: Response status=${response.status}")
            Result.success(response.body())
        } catch (ex: Exception) {
            println("DEBUG_CREATE_BOOKING: FAILED: ${ex::class.simpleName}: ${ex.message}")
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    suspend fun createPaymentIntent(intentInfoRequest: PaymentIntentInfoRequest) : Result<PaymentIntentDto>{
        return try {
            val token = cacheDataSource.getAuthToken()
            println("DEBUG_PAYMENT: Token='${token ?: "NULL"}', Request=$intentInfoRequest")
            val response = httpClient.post(urlString = "${BASE_URL}/payments/intent") {
                setBody(intentInfoRequest)
                header("Authorization", "Bearer $token")
            }
            println("DEBUG_PAYMENT: Response status=${response.status}")
            Result.success(response.body())
        } catch (ex: Exception) {
            println("DEBUG_PAYMENT: FAILED: ${ex::class.simpleName}: ${ex.message}")
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    suspend fun getAllBookings(): Result<List<BookingDto>> {
        return try {
            val response = httpClient.get(urlString = "${BASE_URL}/bookings") {
                header("Authorization", "Bearer ${cacheDataSource.getAuthToken()}")
            }
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}