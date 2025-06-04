import org.gradle.kotlin.dsl.androidTest

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "aps.backflip.curlylab"
    testNamespace = "aps.backflip.curlylab.test"
    compileSdk = 35

    defaultConfig {
        applicationId = "aps.backflip.curlylab"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "aps.backflip.curlylab.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    kapt {
        correctErrorTypes = true
    }

    sourceSets {
        getByName("androidTest") {
            java.srcDir("src/androidTest/java")
            resources.srcDir("src/androidTest/resources")
            manifest.srcFile("src/androidTest/AndroidManifest.xml")
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)


    // Networking
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.json)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.swiperefresh)

    // Utils
    implementation(libs.javax.inject)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}