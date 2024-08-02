# GitHub Browser App

Welcome to the GitHub Browser App! This Android application allows you to browse GitHub repositories and view detailed information using a modern tech stack including Jetpack Compose, GraphQL, and Ktor.

## Features

- Browse GitHub repositories
- Search for repositories by name
- View detailed information about each repository
- User-friendly interface built with Jetpack Compose
- Efficient data fetching using GraphQL
- Lightweight networking with Ktor

## Tech Stack

- **Jetpack Compose**: Modern UI toolkit for building native Android UIs.
- **GraphQL**: A query language for APIs and runtime for executing queries.
- **Ktor**: Asynchronous framework for building connected applications in Kotlin.
- **Kotlin**: The programming language used for development.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Android Studio (Arctic Fox or later recommended)
- Kotlin 1.5 or later
- Basic knowledge of GraphQL and Ktor

## Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/jk2pr/GoGit-Android.git
    ```

2. **Open the project in Android Studio.**

3. **Sync the project with Gradle files:**
    - Go to `File` -> `Sync Project with Gradle Files`.

4. **Build and run the app on an Android device or emulator.**

## Configuration

### Obtain `google-services.json`

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project or select an existing project.
3. In the Firebase Console, navigate to `Project settings` (gear icon) > `General` tab.
4. In the `Your apps` section, select the Android icon to add an Android app.
5. Register your app with the package name used in your project.
6. Download the `google-services.json` file.
7. Place the `google-services.json` file in the `app/` directory of your project.

### Create `secrets.properties`

1. In the root of your project directory, create a file named `secrets.properties`.

2. Add the following properties to `secrets.properties`:

    ```properties
    client_id=YOUR_GITHUB_TOKEN
    client_secret=YOUR_GITHUB_SECRED
    ```

   **Note:** Make sure to replace tokens with your actual GitHub token.

3. Ensure the `secrets.properties` file is included in `.gitignore` to avoid committing sensitive information:
    ```bash
    # Add this line to your .gitignore file
    secrets.properties
    ```

## Usage

- Open the app to view a list of popular GitHub repositories.
- Use the search feature to find repositories by name.
- Tap on a repository to view more details.

## Contributing

We welcome contributions to this project! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


Thank you for using the GitHub Browser App! We hope you find it useful and enjoyable.
