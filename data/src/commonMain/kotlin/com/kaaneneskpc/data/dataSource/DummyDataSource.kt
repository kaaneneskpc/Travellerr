package com.kaaneneskpc.data.dataSource

import com.kaaneneskpc.data.model.TravelListingDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class DummyDataSource {

    private val _listing = MutableStateFlow(createDummyListings())
    val listings = _listing.asStateFlow()

    private fun createDummyListings(): List<TravelListingDto> {
        return listOf(
            TravelListingDto(
                id = "1",
                title = "Luxury Beachfront Villa",
                description = "Wake up to the sound of waves in this stunning beachfront villa. Features modern amenities, private pool, and breathtaking ocean views. Perfect for a romantic getaway or family vacation.",
                location = "Maldives",
                imageUrl = "https://images.unsplash.com/photo-1499793983690-e29da59ef1c2",
                pricePerNight = 450.00,
                rating = 4.9,
                amenities = listOf("WiFi", "Pool", "Beach Access", "Kitchen", "AC"),
                hostName = "Sarah Johnson"
            ),
            TravelListingDto(
                id = "2",
                title = "Cozy Mountain Cabin",
                description = "Escape to this charming cabin nestled in the Swiss Alps. Enjoy hiking trails, stunning mountain views, and cozy evenings by the fireplace.",
                location = "Swiss Alps, Switzerland",
                imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4",
                pricePerNight = 280.00,
                rating = 4.8,
                amenities = listOf("WiFi", "Fireplace", "Heating", "Mountain View", "Parking"),
                hostName = "Hans Mueller"
            ),
            TravelListingDto(
                id = "3",
                title = "Modern City Loft",
                description = "Stylish loft apartment in the heart of Manhattan. Walking distance to Times Square, Broadway theaters, and world-class restaurants.",
                location = "New York City, USA",
                imageUrl = "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00",
                pricePerNight = 350.00,
                rating = 4.6,
                amenities = listOf("WiFi", "Kitchen", "Washer", "City View", "Elevator"),
                hostName = "Michael Chen"
            ),
            TravelListingDto(
                id = "4",
                title = "Tuscan Countryside Villa",
                description = "Experience authentic Italian living in this restored farmhouse surrounded by vineyards and olive groves. Includes wine tasting!",
                location = "Tuscany, Italy",
                imageUrl = "https://images.unsplash.com/photo-1523531294919-4bcd7c65e216",
                pricePerNight = 320.00,
                rating = 4.9,
                amenities = listOf("WiFi", "Pool", "Garden", "Kitchen", "Vineyard"),
                hostName = "Maria Rossi"
            ),
            TravelListingDto(
                id = "5",
                title = "Desert Oasis Resort",
                description = "Luxury resort in the Arabian Desert. Experience camel rides, stargazing, traditional dinners, and breathtaking sunsets.",
                location = "Dubai, UAE",
                imageUrl = "https://images.unsplash.com/photo-1512100356356-de1b84283e18",
                pricePerNight = 500.00,
                rating = 4.7,
                amenities = listOf("WiFi", "Pool", "Spa", "Restaurant", "Desert Safari"),
                hostName = "Ahmed Al-Mansoori"
            ),
            TravelListingDto(
                id = "6",
                title = "Private Island Retreat",
                description = "Your own private island paradise in the Caribbean. Crystal clear waters, white sand beaches, and complete privacy.",
                location = "Bahamas",
                imageUrl = "https://images.unsplash.com/photo-1559827260-dc66d52bef19",
                pricePerNight = 1200.00,
                rating = 5.0,
                amenities = listOf("WiFi", "Private Beach", "Chef", "Boat", "Snorkeling"),
                hostName = "Robert Williams"
            ),
            TravelListingDto(
                id = "7",
                title = "Historic City Apartment",
                description = "Charming apartment in Paris's Latin Quarter. Original 18th-century architecture with modern comforts.",
                location = "Paris, France",
                imageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34",
                pricePerNight = 280.00,
                rating = 4.5,
                amenities = listOf("WiFi", "Kitchen", "Historic Building", "Central"),
                hostName = "Sophie Dubois"
            ),
            TravelListingDto(
                id = "8",
                title = "Tropical Beach Bungalow",
                description = "Authentic Balinese bungalow steps from the beach. Surrounded by rice paddies and tropical gardens.",
                location = "Bali, Indonesia",
                imageUrl = "https://images.unsplash.com/photo-1540541338287-41700207dee6",
                pricePerNight = 120.00,
                rating = 4.8,
                amenities = listOf("WiFi", "Breakfast", "Garden", "Beach", "Spa"),
                hostName = "Wayan Suryani"
            )
        )
    }


}