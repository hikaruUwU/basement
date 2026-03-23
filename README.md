# Base Project - Multi-Terminal Full-Stack Framework

A high-performance, ready-to-use foundation for modern applications, featuring a Spring Boot 3/Java 21 backend, a Vue 3/Vite web frontend, and a Flutter mobile application.

## 🏗 Project Architecture

- **Backend (`/`)**: Spring Boot 3.4.1 powered by Java 21. Uses Mybatis-Flex for flexible database operations.
- **Web Frontend (`/vite`)**: A modern SPA built with Vue 3, Vite, and Element Plus. Supports Capacitor for mobile packaging.
- **Mobile Frontend (`/flutter`)**: Cross-platform mobile app built with Flutter.
- **Automation Tools**: Integrated `AMVCGenerator` and `AxiosGenerator` to synchronize database schemas and API definitions between backend and frontend.

## 🛠 Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.11
- **ORM**: Mybatis-Flex (includes SQL auditing and execution plan analysis)
- **Security**: Session-based lightweight authentication with Argon2 password hashing.
- **Data Handling**: MapStruct (DTO mapping), Log4j2 (Auditing).

### Frontend
- **Web**: Vue 3, Pinia (State Management), Vue-Router, Axios, Element Plus.
- **Mobile**: Flutter 3.x.

## 🚀 Quick Start

### Prerequisites
- JDK 21+
- Maven 3.9+
- Node.js 18+ & pnpm
- Flutter SDK (for mobile development)

### Backend Setup
1. Configure your database in `src/main/resources/application-dev.properties`.
2. Run `./mvnw spring-boot:run` or launch the `BaseApplication` class.

### Web Frontend Setup
1. Navigate to the directory: `cd vite`
2. Install dependencies: `pnpm install`
3. Start dev server: `pnpm dev`

## 💎 Key Features

- **SQL Lighthouse (`@EnableSqlLighthouse`)**: Real-time SQL execution plan analysis and time consumption logging.
- **Performance Seer (`@EnablePerformanceSeer`)**: Automatic method-level performance tracing across the Controller-to-Repository chain.
- **Sensitive Data Masking (`@Sensitive`)**: Annotation-driven data desensitization for sensitive fields like phone numbers, emails, and passwords.
- **Automated API Sync**: Run `AxiosGenerator.java` to instantly generate `GAxios.ts` (API methods) and `GType.ts` (TypeScript interfaces) for the frontend.
