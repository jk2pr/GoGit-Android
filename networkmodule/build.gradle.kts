plugins {
   // alias(libs.plugins.android.application)
    //id("com.android.library")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinx.serializtion)
}

android {
    namespace = "com.hoppers.networkmodule"
    compileSdk = 36

    defaultConfig {
        //applicationId = "com.hoppers.networkmodule"
        minSdk = 29
        //testOptions.targetSdk = 34
      //  versionCode = 1
       // versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.apollo.runtime)
    implementation( libs.logging.interceptor)
    implementation(libs.ktor.clientloggging)
    implementation(libs.ktor.content.negotioation)
    implementation(libs.ktor.kotlinx.json)
    //implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
}