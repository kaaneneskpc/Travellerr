# ✈️ Travellerr - Kotlin Multiplatform Travel Booking App

![Android](https://img.shields.io/badge/Android-✅-green)
![iOS](https://img.shields.io/badge/iOS-✅-blue)
![Web(WASM)](https://img.shields.io/badge/Web(WASM)-✅-lightblue)
![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-orange)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-%F0%9F%9A%80-orange)
![Koin](https://img.shields.io/badge/Koin-DI-yellow)
![Open Source](https://img.shields.io/badge/Open%20Source-%E2%9C%94%EF%B8%8F-lightgrey)

---

## 🚀 About the Project

**Travellerr** is a modern, modular, and scalable travel booking application targeting Android, iOS, and Web (WASM) platforms. Built with Kotlin Multiplatform (KMP) and Jetpack Compose Multiplatform, it allows users to explore travel destinations, book trips, manage bookings, and process payments seamlessly. Features include **glassmorphism UI design**, **Stripe payment integration**, **iOS Live Activities** for real-time order tracking, and advanced **gamification features**.

---

## 📦 Features

### ✈️ Customer Features
- 🔥 **Kotlin Multiplatform**: Single codebase for Android, iOS, & Web (WASM)
- 🎨 **Glassmorphism UI**: Modern, animated interface with Canvas-based backgrounds
- 🗺️ **Travel Listings**: Browse beautiful destinations with detailed information
- 📅 **Trip Date Selection**: Choose from available trip dates with capacity tracking
- 👥 **Guest Management**: Select number of guests for bookings
- 🔍 **Availability Check**: Real-time availability verification before booking
- 🛒 **Booking Management**: Full booking lifecycle with status tracking
- 💳 **Stripe Payment Integration**: Secure payment processing
- 📱 **iOS Live Activities**: Real-time booking tracking on Lock Screen and Dynamic Island
- 🎮 **Gamification**: Leaderboard rankings and Spin Wheel for daily prizes
- 🔔 **Push Notifications**: Personalized booking updates

### 📊 Booking Status Management
- ⏳ **PENDING**: New bookings awaiting confirmation
- ✅ **CONFIRMED**: Approved bookings ready for travel
- 🏁 **COMPLETED**: Finished trips
- ❌ **CANCELLED**: Cancelled bookings

### 🏗️ Technical Features
- 🏗️ **Clean Architecture**: Domain → Data → Presentation layers
- 🔐 **Secure Authentication**: User management with session handling
- 🏗️ **Dependency Injection with Koin**: Modular and testable architecture
- 🌐 **Networking with Ktor**: Multiplatform HTTP client
- 🖼️ **Image Loading with Coil**: Platform-specific caching (25% memory, 100MB disk)
- 🧪 **Comprehensive Unit Testing**: Tests with Turbine, Coroutines Test
- 📄 **Cursor-based Pagination**: Efficient data fetching
- ⚡ **Code Splitting**: Module-based lazy loading for faster startup

---

## 🏗️ Project & Module Structure

```
- composeApp/      : Multiplatform main entry point and UI (Presentation layer)
    - ui/
        - listing/         : Home screen with travel listings
        - details/         : Travel detail screen
        - checkout/        : Booking checkout with payment
        - bookings/        : Booking list and detail screens
    - navigation/          : Navigation graph and routing
    - theme/               : App theming and colors
    - widgets/             : Reusable UI components

- iosApp/          : iOS app entry point and Swift-specific code
    - LiveActivityBridge   : iOS Live Activities integration
    - Widget Extension     : Lock Screen and Dynamic Island widgets

- domain/          : Core domain models, interfaces, and UseCases
    - model/               : Business models (Booking, TravelListing, TripDate)
    - repository/          : Repository interfaces
    - usecase/             : Business logic use cases

- data/            : Repository implementations, data sources, DTOs
    - repository/          : Repository implementations
    - dataSource/          : Remote data sources
    - model/               : DTOs and request models
    - mappers/             : DTO to Domain mappers

- presentation/    : ViewModels and UI state management
    - feature/
        - listings/        : TravelListingViewModel
        - bookings/        : BookingListViewModel, BookingDetailViewModel
        - checkout/        : CheckoutViewModel
```

---

## 🧩 Architecture

- **Clean Architecture**: Data → Domain → Presentation layers
- **MVI Pattern**: Model-View-Intent for state management
- **Feature-based modules**: Independent development and testing
- **Centralized DI with Koin**: All ViewModels and repositories managed centrally
- **Testability**: Mockable repository interfaces and ViewModels

---

## 🛠️ Tech Stack

### Core Technologies
| Technology | Purpose |
|------------|---------|
| **Kotlin Multiplatform (KMP)** | Shared business logic across platforms |
| **Jetpack Compose Multiplatform** | Modern declarative UI |
| **Koin** | Dependency Injection |
| **Ktor** | HTTP Client & API Integration |
| **Navigation3** | Multiplatform screen routing |
| **DataStore** | Cross-platform local persistence |

### UI & Design
| Technology | Purpose |
|------------|---------|
| **Material3** | Modern UI components |
| **Custom Canvas Drawing** | Animated backgrounds & glassmorphism |
| **Coil3** | Image loading with caching |
| **Glassmorphism Design** | Semi-transparent glass effects |

### Payments & Integrations
| Technology | Purpose |
|------------|---------|
| **Stripe SDK** | Payment processing |
| **iOS Live Activities** | Real-time tracking (iOS 16.1+) |
| **Coroutines & Flow** | Reactive data management |

---

## 🎨 Glassmorphism Design System

Travellerr implements a cohesive glassmorphism design language:

### Color Palette
- **TealPrimary**: `#00897B`
- **TealLight**: `#4DB6AC`
- **TealDark**: `#1B3A36`
- **SoftCream**: `#F5F0E8`
- **WarmOrange**: `#FF6B35`

### Glass Components
- `GlassHeader` - Profile chip with gradient avatar
- `GlassSearchBar` - Search with gradient filter button
- `GlassCategoryChip` - Selectable category filters
- `GlassTabBar` - Tab navigation for booking status
- `GlassBookingListItem` - Booking cards with slanted images
- `GlassLoadingIndicator` - Loading states
- `GlassErrorCard` - Error display

### Animated Elements
- `HomeAnimatedBackground` - Dynamic Canvas gradients
- `AnimatedAbstractBackground` - Pulse and wave effects
- `GlowingStatusDot` - Pulsing status indicators
- Wave text decoration with Canvas drawing

---

## 📱 Screens

### 🏠 Home Listing Screen
- Animated Canvas background with radial gradients
- Glass header with user profile and notifications
- Welcome section with animated wave underline
- Glass search bar with filter options
- Category chips (Beach, Mountain, City, Forest, Desert)
- Featured Destinations with large immersive cards
- Popular Now section with compact cards

### 📋 Booking List Screen
- Tab navigation with HorizontalPager
- Glass tab bar for status filtering
- Status-colored dots with glow animation
- Slanted image shapes for visual interest
- Real-time refresh on navigation

### 📄 Booking Detail Screen
- Full-bleed header image with slanted shape
- Glass back button overlay
- Comprehensive info box with status and payment
- Action buttons (Confirm, Complete, Cancel)
- Price display with currency

### 💳 Checkout Screen
- Trip date selection
- Guest count management
- Availability checking
- Price calculation
- Stripe payment integration

---

## 🎉 Recent Updates

### 🎨 Soft Mint Glassmorphism UI (Latest)
- **HomeListingScreen**: Complete redesign with animated Canvas background
- **TravelDetailScreen**: Glassmorphic property details, gallery, and layout updates
- **CheckoutScreen**: Soft Mint styling with integrated Glassmorphism containers
- **Featured Destination Cards**: Large immersive cards (280x360dp) with gradient overlays
- **Popular Destination Cards**: Compact glass cards (180x220dp)
- **Category Chips**: Selectable filters with color states
- **Glass Components**: Consistent design language across all screens

### 🔄 State Refresh Pattern
- **LaunchedEffect Integration**: Screens refresh data on every composition
- **Real-time Updates**: Booking status changes reflect immediately
- **ViewModel Pattern**: MutableStateFlow with collectAsState()

### 📱 iOS Live Activities (iOS 16.1+)
- **Lock Screen Tracking**: Real-time booking status on Lock Screen
- **Dynamic Island**: Compact and expanded states
- **LiveActivityBridge**: Swift integration with Kotlin expect/actual pattern

### 🎮 Gamification System
- **Leaderboard**: Compete with other travelers
- **Spin Wheel**: Daily prizes with animated UI
- **Custom Canvas Drawing**: Animated spin wheel components

---

## 🚀 Installation & Running

### Requirements
- **JDK 17+**
- **Android Studio Hedgehog or newer**
- **Xcode 15+ (for iOS)**

### Clone the Project

```sh
git clone https://github.com/kaaneneskpc/Travellerr.git
cd Travellerr
```

### Run for Android

```sh
# macOS/Linux
./gradlew :composeApp:assembleDebug

# Windows
.\gradlew.bat :composeApp:assembleDebug
```

### Run for iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select target device/simulator
3. Build and run

### Run for Web (WASM)

```sh
./gradlew :composeApp:wasmJsBrowserRun
```

---

## 🧪 Testing

### Unit Tests
```sh
./gradlew :domain:test :data:test :presentation:test
```

### Testing Patterns
- **Arrange-Act-Assert**: Standard test convention
- **Fake Repositories**: In-memory implementations for testing
- **Flow Testing**: Turbine for StateFlow assertions
- **Coroutines Test**: StandardTestDispatcher for ViewModel tests

---

## 🔐 Security

### Payment Security
- **Stripe SDK**: PCI-compliant payment processing
- **Server-side Validation**: Payment intents created on backend
- **Secure Token Handling**: No card data stored locally

### Data Security
- **HTTPS Only**: All API communications encrypted
- **Session Management**: Secure user authentication
- **Input Validation**: Server and client-side validation

---

## 🧑‍💻 Contributing

Contributions are welcome! 🎉

### Contribution Steps

1. Fork 🍴
2. Create a new branch 🚀 (`git checkout -b feature-name`)
3. Commit your changes 🎯 (`git commit -m 'Description'`)
4. Push the branch 📤 (`git push origin feature-name`)
5. Open a Pull Request 🔥

### Areas for Contribution
- **UI Enhancements**: New animations, responsive design improvements
- **New Features**: Wishlist, reviews, social sharing
- **Performance**: Image caching, lazy loading optimizations
- **Testing**: Unit tests, UI tests
- **Documentation**: Code documentation, user guides

---

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

---

## 📚 Resources & Inspiration

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Jetpack Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Koin DI](https://insert-koin.io/)
- [Ktor](https://ktor.io/)
- [Coil](https://coil-kt.github.io/coil/)
- [Canvas Drawing in Compose](https://developer.android.com/jetpack/compose/graphics/draw/overview)
- [iOS Live Activities](https://developer.apple.com/documentation/activitykit)

---

> **Note:** For detailed architectural explanations and patterns, see the [PRD document](./prd.md).

---

## Contact

**Kaan Enes Kapıcı**
- LinkedIn: [Kaan Enes Kapıcı](https://www.linkedin.com/in/kaaneneskpc/)
- GitHub: [@kaaneneskpc](https://github.com/kaaneneskpc)
- Email: kaaneneskpc1@gmail.com

💡 **Open to feedback and collaboration!** If you're interested in modern mobile architecture, travel apps, or multiplatform development, feel free to connect. 🚀
