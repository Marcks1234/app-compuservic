plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.app_compuservic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app_compuservic"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))  // dependecia para el gestor de versiones  FireBase
    implementation("com.google.firebase:firebase-analytics-ktx") // analisis de problemas en usuarios
    implementation("com.google.firebase:firebase-auth-ktx") // autenticacion
    implementation("com.google.firebase:firebase-firestore-ktx") // base de datos no relacional

    implementation("androidx.navigation:navigation-compose:2.8.0-alpha08") // dependecia para navegar

    //nuevas implementaciones
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")

    implementation ("io.coil-kt:coil-compose:2.1.0")// Coil Compose for image loading

    //Storage
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation ("androidx.compose.runtime:runtime:1.6.0") // versión según tu Compose
    implementation ("androidx.compose.material3:material3:1.1.2")
    implementation ("androidx.navigation:navigation-compose:2.7.3")
    implementation ("io.coil-kt:coil-compose:2.5.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.10.1")
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")

    implementation("com.google.firebase:firebase-storage-ktx")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")


    implementation ("org.json:json:20211205")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}