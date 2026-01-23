import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.bps"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.silawet"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.6.4"

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
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            // 2. Gunakan .set() dan Enum JvmTarget
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    val composeBom = platform("androidx.compose:compose-bom:2025.08.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.retrofit)

    // Untuk Konverter Gson (JSON ke Kotlin Data Class)
    implementation(libs.com.squareup.retrofit2.converter.gson)

    // Untuk menghubungkan ViewModel dengan Compose (menyediakan fungsi viewModel())
    implementation(libs.lifecycle.viewmodel.compose)

    // Choose one of the following:
    // Material Design 3
    implementation(libs.androidx.material3)
    // or Material Design 2
    //implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
    //implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    //implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    //implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    //implementation("androidx.compose.material3.adaptive:adaptive")

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with LiveData
    implementation(libs.androidx.runtime.livedata)
    // Optional - Integration with RxJava
    //implementation("androidx.compose.runtime:runtime-rxjava2")

    // Optional - librarys for pager tab
    implementation(libs.androidx.foundation)

    // Optional - Library Coil
    implementation(libs.coil.kt.coil.compose)

    implementation(libs.androidx.material.icons.extended)

    implementation(libs.mpandroidchart)

}