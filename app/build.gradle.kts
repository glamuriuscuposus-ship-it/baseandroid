plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.sbase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sbase"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            val storePath = providers.gradleProperty("SBASE_STORE_FILE")
                .orElse("C:/jajabinx/baseandroid-main/release-key.jks")
                .get()
            storeFile = file(storePath)
            storePassword = providers.gradleProperty("SBASE_STORE_PASSWORD")
                .orElse(System.getenv("SBASE_STORE_PASSWORD") ?: "123456")
                .get()
            keyAlias = providers.gradleProperty("SBASE_KEY_ALIAS")
                .orElse("sbase")
                .get()
            keyPassword = providers.gradleProperty("SBASE_KEY_PASSWORD")
                .orElse(System.getenv("SBASE_KEY_PASSWORD") ?: "123456")
                .get()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.webkit:webkit:1.11.0")
}
