# Travellerr PRD (Product Requirements Document)

## 1. Project Purpose and Overview

Travellerr is a modern, modular, and scalable multiplatform application targeting Android and iOS, developed with Kotlin Multiplatform (KMP) and Jetpack Compose Multiplatform. The project enforces clean architecture, advanced gamification features, iOS Live Activities, code splitting, and strict coding standards. It aims to provide a seamless, high-performance user experience with custom animations, optimized media handling, and MVI state management.

---

## 2. Technologies Used

### Core Technologies
- **Kotlin Multiplatform (KMP):** Shared business logic, domain models, and data layer across Android and iOS targets.
- **Jetpack Compose Multiplatform:** Modern, declarative UI framework used for all rendering.
- **Koin:** Dependency Injection (Core, Compose, ViewModel, Android).
- **Ktor:** Multiplatform HTTP client for network operations (Content Negotiation, Logging, OkHttp/Darwin).
- **JetBrains Navigation3:** Multiplatform screen routing (`org.jetbrains.androidx.navigation3`).
- **DataStore:** Cross-platform local persistence.

### UI & UX Technologies
- **Material3:** Modern UI components and responsive design conventions.
- **ConstraintLayout & Standard Layouts:** Building complex interfaces using Compose Box/Row/Column and ConstraintLayout.
- **Custom Canvas Drawing:** Used for customized UI components like Gamification Spin Wheels.
- **Coil3:** Image loading with **enhanced caching** (Memory cache 25%, Disk cache 100MB) via `CachedAsyncImage` and platform-specific `ImageLoaderFactory`.
- **Loading Shimmer Effects:** Standardized visual placeholders while loading resources.
- **Lazy Loading & Pagination:** Cursor-based pagination (`PaginatedResult<T>`) connected to `LazyListState`.

### Payments & External Integrations
- **Stripe SDK:** Payment processing via `stripe-android` integration.
- **iOS Live Activities (iOS 16.1+):** Real-time order tracking using `ActivityConfiguration` and `WidgetKit` bridging to Swift `LiveActivityBridge`.
- **Coroutines & Flow / StateFlow:** Reactive data and UI state management.

---

## 3. Project and Module Structure

```text
- composeApp/      : Multiplatform main entry point and general UI (Presentation layer).
- iosApp/          : iOS app entry point and Swift-specific code (LiveActivityBridge, iOS UI setup).
- domain/          : Core domain models, interfaces, and UseCases (No platform dependencies).
- data/            : Repository implementations, remote/local data sources, DTOs.
- gradle/          : Dependency version catalog (`libs.versions.toml`) and build scripts.
```

*(Note: Features are logically split into sections based on `ModuleType` enum definitions: `ADMIN`, `SECONDARY`, `GAMIFICATION`, `CORE`, `AUTH`, `MAIN`.)*

---

## 4. Architecture and Clean Architecture

### Layers and Technologies Used

- **Presentation Layer (`composeApp/`)**
  - **Pattern:** MVI (Model-View-Intent). States are managed by ViewModels and triggered/rendered in composables.
  - **Auth Flow:** Standardized screens managed here (Splash Screen, Login, Register, Forgot Password, Verify Email).
  - Use of `keepAlive` when UI state needs to be retained.
  - **Technologies Used:** Jetpack Compose Multiplatform, Material3, Navigation3 UI, Koin ViewModels, Flow/StateFlow.

- **Domain Layer (`domain/`)**
  - Core business models.
  - Repository interfaces (e.g., `GamificationRepository`, auth interfaces).
  - Follows SOLID and Single Responsibility Principles.
  - **Technologies Used:** Kotlin Multiplatform, Coroutines.

- **Data Layer (`data/`)**
  - Repository implementations (Repository Pattern).
  - Handles caching, pagination cursors, local storage, and remote APIs via Ktor.
  - **Technologies Used:** Ktor Client, kotlinx.serialization, DataStore.

- **DI Layer (Koin Modules)**
  - Managed via predefined separate Koin modules: `coreModule`, `authModule`, `mainModule`, `adminModule`, `secondaryModule`, `gamificationModule`.

### Code Splitting Pattern (Modularization)
- **Lazy Module Loading:** Uses a `ModuleLoader` object.
- **ModuleType Enum:** Defines module boundaries (ADMIN, SECONDARY, GAMIFICATION).
- **Loading Strategy:**
  - `loadModuleIfNeeded(moduleType)` is called on-demand.
  - `isModuleLoaded(moduleType)` checks module status.
  - Only core and auth modules are loaded at app startup.
  - Feature modules are loaded right before navigation to their respective screens.

---

## 5. Core Feature Patterns

### 5.1. Pagination Pattern
- **Logic:** Cursor-based pagination for Firestore/remote queries.
- **Data Structures:** `PaginatedResult<T>` containing `items`, `lastDocumentId`, `hasNextPage`.
- **State Management:** `PaginationState` sealed class (Idle, Loading, LoadingMore, EndReached, Error).
- **Trigger:** Uses `LazyListState` and `snapshotFlow` to automatically load the next page when the user is near the end (e.g., 3 items before the end).

### 5.2. Image Loading Pattern
- **Component:** `CachedAsyncImage` must be used for all remote product/asset images.
- **Implementation:** Platform-specific `ImageLoaderFactory` (expect/actual pattern).
- **Configuration:** 25% of RAM for memory cache, 100MB for disk cache.
- **UX:** Always show shimmer placeholders while loading, and a fallback icon for error states.

### 5.3. Gamification Pattern
- **Repository:** `GamificationRepository` for leaderboard and spin wheel data tracking.
- **State:** Uses `RequestState` for loading states in ViewModels.
- **UI:** Includes animated components utilizing `AnimatedVisibility`, `LaunchedEffect`, and `Canvas` customized custom spin wheel drawing.
- **Logic:** Tracks spin cooldowns, prize histories per user, and user points/rankings in a remote `leaderboard` collection.

### 5.4. iOS Live Activities Pattern (iOS 16.1+)
- **API:** Expect/actual `LiveActivityManager` pattern for cross-platform availability.
- **Implementations:** Android is a no-op (stub); iOS calls the Swift `LiveActivityBridge`.
- **Mechanics:** 
  - `OrderTrackingObserver` triggers activities on order creation or updates.
  - Uses `OrderTrackingAttributes` with static order info and `ContentState` for dynamic data.
  - Creates Lock Screen views and Dynamic Island states (compact, minimal, expanded) using WidgetKit (`ActivityConfiguration`).
- **Lifecycle:** Stores `activityId` for updates and ends activities automatically on `DELIVERED` or `CANCELLED`.
- **Note:** Widget Extension requires manual Xcode target creation.

---

## 6. General Coding Guidelines & Best Practices

### Basics
- **Language:** English for all code and documentation.
- **Immutability:** Prefer immutability. Use `readonly` / `val` for literals that don't change.
- **Typing:** Always declare types for variables and functions. Avoid using `Any`. Create necessary types.
- **RO-RO Pattern:** Reduce function parameters by passing an object and returning a result object.

### Functions and Nomenclature
- **Naming:** 
  - `PascalCase` for classes.
  - `camelCase` for variables, functions, and methods.
  - `underscores_case` for file/directory names.
  - `UPPERCASE` for constants/environment variables.
- **Functions:** Start with a verb (`isX`, `hasX`, `canX`, `executeX`, `saveX`).
- **Length:** Break functions down to < 20 instructions. 
- **Nesting:** Avoid nesting with early checks and returns. Use higher-order functions (`map`, `filter`, `reduce`).

### Classes and Data
- **Size:** Keep classes small (< 200 instructions, < 10 public methods, < 10 properties).
- **Types:** Use `data class` for data. Avoid abusing primitive types; encapsulate them.
- **SOLID:** Prefer composition over inheritance. Define contracts with interfaces. Avoid validating data within functions; use wrapper classes with internal validation.

### Exceptions
- Use exceptions strictly to handle unexpected errors, correct an expected problem, or add context. Do not use them for normal control flow. Otherwise, rely on a global handler.

---

## 7. Testability

### 7.1. Unit Testing Pattern
- **Source Set:** Use `commonTest` for all unit tests.
- **Frameworks:**
  - `kotlin-test` for multiplatform assertions.
  - `kotlinx-coroutines-test` for suspending functions (`runTest(testDispatcher)`).
- **Coroutines Testing:** 
  - ViewModels tested using `StandardTestDispatcher`.
  - Use `Dispatchers.setMain(testDispatcher)` in `@BeforeTest`.
  - Use `Dispatchers.resetMain()` in `@AfterTest`.
  - Utilize `advanceUntilIdle()` for coroutine completion checks.
- **Flow Testing:** Use `turbine` (`flow.test { awaitItem(); cancelAndIgnoreRemainingEvents() }`). Handle the initial Loading state before verifying the expected state in `StateFlows`.

### 7.2. Fake Repository Pattern
- Place fake implementations in `data/src/commonTest/kotlin/.../fake/` directory.
- Maintain data in-memory using mutable lists/maps.
- Integrate a `shouldReturnError` flag to easily test error scenarios (`emitLoading()`, `emitError()`).
- Add specific helper methods: `setProducts()`, `clearFavorites()`, etc.

### 7.3. ViewModel Testing Pattern
- Utilize fake repositories tailored for ViewModel tests.
- Control state emissions via `MutableStateFlow` from fakes.
- Assert interactions:
  - Test initial, success, error, and empty states.
  - Verify callback invocations locally (using boolean flags or captured values).
- Follow the **Arrange-Act-Assert** convention consistently. Name variables explicitly (`inputX`, `mockX`, `actualX`, `expectedX`).

---

## 8. UX and Modern Design

### 8.1. UI/UX Features
- **Design System:** Implement modern Material 3 properties. Avoid using XML entirely.
- **Layouts:** Build responsive and engaging interfaces via Jetpack Compose standard layout components (`Box`, `Row`, `Column`) and `ConstraintLayout`.
- **Loading Experience:** Utilize shimmer effects strictly with `CachedAsyncImage` to deliver a continuous, high-performance UI experience during rendering.

---

This document incorporates all system guidelines and architectural structures to ensure that **Travellerr** strictly follows a scalable, modular, and testable framework utilizing KMP and specific cross-platform best practices.
