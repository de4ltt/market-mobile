# Market-Mobile

An Android application built with Kotlin and Jetpack Compose designed for retail and warehouse management. The app provides various tools for different employee roles (Director, Commodity Expert, Storekeeper, Seller) to manage shifts, track working hours, control supply shipments, handle warehouse inventory, and view corporate directories.

<img width="3320" height="4840" alt="Group 2" src="https://github.com/user-attachments/assets/5b71a8a8-89f4-4cc5-a752-466903a7b4fa" />

---

## Tech Stack

*   **Language:** Kotlin 2.2.0
*   **UI Framework:** Jetpack Compose (Compose BOM 2025.12.01) with Edge-to-Edge and Material 3
*   **Architecture:** Clean Architecture / MVVM with a multi-module structure
*   **Dependency Injection:** Dagger Hilt 2.58 + Hilt Navigation Compose
*   **Networking:** Ktor Client 3.3.3 (CIO engine, Auth/Bearer, ContentNegotiation, Logging)
*   **Local Cache:** Room DB 2.6.1 for offline access to employee and vacation data
*   **Preferences / Session Storage:** Jetpack DataStore Preferences 1.2.0
*   **Navigation:** Jetpack Navigation Compose 2.8.5 with type-safe routing via Kotlin Serialization
*   **Image Loading:** Coil 3.3.0
*   **Build System:** Gradle Kotlin DSL (`.gradle.kts`) with version catalog (`libs.versions.toml`)
*   **SDK Support:** Compile/Target SDK 36, Min SDK 26

---

## Architecture & Modules

The project is structured into three main layers:
1.  **`app`** — The entry point of the application.
2.  **`core`** — Shared modules containing networking, database caching, design system tokens, and business models.
3.  **`feature`** — Isolated functional modules containing screens, states, and ViewModels.

### Module Breakdown

| Module | Purpose |
| :--- | :--- |
| **`:app`** | Application startup configurations (Application class, MainActivity). Wires navigation and routes together. |
| **`:core:common`** | Common helper utilities and custom serializers for BigDecimal, LocalDate, and LocalDateTime. |
| **`:core:model`** | Domain business models (Product, Employee, Vacation, Role, etc.) and repository interfaces. |
| **`:core:database`** | Room local database implementation. Caches employees and vacations for offline access. |
| **`:core:network`** | Ktor HTTP client provider, API service configurations, and user preferences storage. |
| **`:core:data`** | Orchestrates active sessions, handles login/logout state, tracks check-in/out for shifts, and contains data mapping utilities. |
| **`:core:ui`** | Design system containing colors, typography, and reusable layout components. |
| **`:feature:auth`** | Provides authorization screens and validation logic. |
| **`:feature:mainmenu`** | The landing dashboard showing features corresponding to the employee's role. Provides quick actions to start/end shifts. |
| **`:feature:employees`** | For managers to list staff, register new employees, manage shifts, and handle vacation request reviews. |
| **`:feature:shift`** | Employee portal displaying personal details, working hours, and vacation request statuses. |
| **`:feature:receival`** | Product reception flow supporting barcode inspection and write-offs with reason comments. |
| **`:feature:products`** | Inventory catalog. Manages storage locations, shelves, products, price calculations, and orders. |
| **`:feature:dictionaries`** | Viewer for corporate directories such as counterparties, contacts, contracts, and trucks. |

---

## Architectural Highlights

### 1. Generic Lists (UiDisplayable Pattern)
To minimize UI duplication, a generic list rendering engine is implemented:
*   The `UiDisplayable` interface defines generic fields like name, barcode, and a key-value fields map.
*   Domain entities are mapped to this interface using extension functions.
*   A single generic screen (`ItemsRepresentationScreen`) handles search, filtering, and listing layout for all entities without duplicating Compose UI code.

### 2. Auto Token Refresh
The network module configures Ktor's `Auth` plugin with bearer token authentication. Whenever an API request returns an HTTP 401, the client transparently initiates a token refresh request, updates the local DataStore, and retries the original request without user interruption.

### 3. Shift Verification (Midnight Check-In Dialog)
If an employee is active on shift at midnight, the app displays a confirmation dialog asking to approve the daily overtime report. The shift status is updated accordingly based on user response or timeout.

---

## Build & Setup

### Requirements
*   Android Studio Ladybug or newer
*   JDK 17 or JDK 21
*   Android SDK Build-Tools 36+

### Installation
1.  Clone the repository:
    ```bash
    git clone https://github.com/de4ltt/market-mobile.git
    ```
2.  Open the project in Android Studio.
3.  Let Gradle sync finish.
4.  Configure the API gateway URL (`BASE_URL_GATE`) in `core/network/src/main/java/ru/kubsu/market/core/network/ApiConfig.kt`.
5.  Run the application on a device or emulator running Android 8.0 (API 26) or higher.
