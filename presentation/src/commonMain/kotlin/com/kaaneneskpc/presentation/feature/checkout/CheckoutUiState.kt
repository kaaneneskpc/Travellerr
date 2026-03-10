package com.kaaneneskpc.presentation.feature.checkout

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.BookingAvailability
import com.kaaneneskpc.domain.model.PaymentIntent
import com.kaaneneskpc.domain.model.TravelListing

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val listing: TravelListing? = null,
    val selectedTripDateId: String? = null,
    val numberOfGuests: Int = 1,
    val isCheckingAvailability: Boolean = false,
    val availabilityErrorMessage: String? = null,
    val bookingAvailability: BookingAvailability? = null,
    val creatingBooking: Boolean = false,
    val paymentIntent: PaymentIntent? = null,
    val booking: Booking? = null,
    val hasDateConflict: Boolean = false,
)