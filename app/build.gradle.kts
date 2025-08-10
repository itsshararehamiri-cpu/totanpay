plugins {
    alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
  //  alias(libs.plugins.compose.compiler)
}
android {
    namespace = "com.example.totanpay"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.totanpay"
        minSdk = 21
        targetSdk = 35
        versionCode = 9
        versionName = "1.6.0"
        multiDexEnabled =true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources=true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += listOf("psp", "device")
    productFlavors {
        create("pn") {
            dimension = "psp"
            applicationIdSuffix = ".pn"
            isDefault=true
            resValue("string", "app_name", "پرداخت نوین")
        }
        create("fanava") {
            dimension = "psp"
            applicationIdSuffix = ".fa"
            resValue("string", "app_name", "فناوا")
        }
        create("i5000") {
            dimension = "device"
        }
        create("i9000") {
            dimension = "device"
        }
    }
    sourceSets.getByName("pn") {
        java.setSrcDirs(listOf("src/pn/java"))
    }
    sourceSets.getByName("fanava") {
        java.setSrcDirs(listOf("src/fanava/java"))
    }
    sourceSets.getByName("i5000") {
        java.setSrcDirs(listOf("src/i5000/java"))
    }
    sourceSets.getByName("i9000") {
        java.setSrcDirs(listOf("src/i9000/java"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(files("libs\\urovosdkLibs_New_v1.0.13.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coroutines.android)
    api("org.jpos:jpos:1.9.6")
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.activity.compose)
    api(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.constraintlayout)
    implementation(libs.android.gson)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.room.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation("com.github.razaghimahdi:Compose-Persian-Date-Picker:1.1.1")
    implementation("com.github.samanzamani:PersianDate:1.7.1")
    implementation(libs.androidx.multidex)
    implementation(libs.slf4j.api)
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation(libs.okhttp)
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.1")
    implementation(libs.android.gson)
    implementation("com.journeyapps:zxing-android-embedded:3.5.0")
}
