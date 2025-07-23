# ChatMate Android App

A modern real-time chat application built with Kotlin and Jetpack Compose.

## Technologies Used

- **Kotlin** - Modern programming language for Android
- **Jetpack Compose** - Modern UI toolkit for building native Android UI
- **Hilt** - Dependency injection framework
- **Coroutines** - Asynchronous programming
- **MVVM Architecture** - Model-View-ViewModel pattern for clean code organization
- **Firebase** - Backend services for real-time messaging
  - Firebase Authentication
  - Firebase Firestore

## Features

- Real-time messaging
- User authentication
- Modern Material Design 3 UI
- Message status indicators (sent, delivered, read)
- Typing indicators
- Online/offline status
- Push notifications

## Setup Instructions

### 1. Firebase Setup

Before running the app, you need to set up Firebase:

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Add an Android app to your project
4. Enter the package name: `com.chatmate`
5. Download the `google-services.json` file
6. Replace the placeholder `google-services.json` file in the `app/` directory

### 2. Firebase Configuration

Enable the following Firebase services in your project:

1. **Authentication**
   - Go to Authentication > Sign-in method
   - Enable "Anonymous" authentication

2. **Firestore Database**
   - Go to Firestore Database
   - Create database in test mode (for development)
   - Set up security rules as needed

3. **Cloud Messaging** (Optional)
   - Go to Cloud Messaging
   - No additional setup required for basic functionality

### 3. Building the App

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Build and run the app

## Project Structure

```
app/src/main/java/com/chatmate/
├── data/
│   ├── model/          # Data models (User, Message, ChatRoom)
│   ├── repository/     # Repository implementations
│   └── remote/         # Firebase and FCM services
├── domain/
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic use cases
├── presentation/
│   └── viewmodel/      # ViewModels for UI state management
├── ui/
│   ├── components/     # Reusable UI components
│   ├── screens/        # Screen composables
│   └── theme/          # UI theme and styling
└── di/                 # Dependency injection modules
```

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: Data layer with repositories and Firebase integration
- **View**: UI layer built with Jetpack Compose
- **ViewModel**: Presentation layer that manages UI state and handles user interactions

## Dependencies

Key dependencies used in this project:

- Jetpack Compose BOM
- Hilt for dependency injection
- Firebase BOM with Auth, Firestore, and Messaging
- Kotlin Coroutines
- Navigation Compose
- Material 3 Design
- Kotlinx DateTime

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is for educational purposes.
