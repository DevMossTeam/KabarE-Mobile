plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services") // Jika digunakan
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.devmoss.kabare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.devmoss.kabare"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Ubah ke true untuk produksi
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude ("META-INF/LICENSE.md")
        exclude("META-INF/NOTICE.md")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.9.0")

    // Navigation components
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.1")

    // Material Design components
    implementation("com.google.android.material:material:1.10.0-alpha04")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.espresso.core)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Picasso for image loading
    implementation("com.squareup.picasso:picasso:2.8")
    implementation ("de.hdodenhof:circleimageview:3.1.0")


    // CardView for material card UI
    implementation("androidx.cardview:cardview:1.0.0")

    // Firebase Authentication and Google Sign-In
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.android.gms:play-services-auth:19.0.0")

    // Retrofit and OkHttp for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // JSoup for HTML parsing
    implementation("org.jsoup:jsoup:1.16.1")

    // Email dependencies for SMTP (use only if you're handling email via SMTP directly)
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("org.jsoup:jsoup:1.15.3")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")// untuk refresh

}
