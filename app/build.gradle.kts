plugins {
    alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
//   alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.kotlinAndroidKsp)
//    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.example.totanpay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.totanpay"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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
    implementation(files("E:\\myprojects\\TotanPay\\app\\libs\\urovosdkLibs-v62.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coroutines.android)
    //implementation(libs.androidx.room.ktx)
//   implementation(libs.androidx.room.compiler)
//    implementation(libs.androidx.room.runtime)
   // implementation(libs.androidx.room.ktx)
    // api(libs.jpos.module)
//    api(platform("org.jpos:jpos-bom:1.9.6"))
//    api("org.jpos:jpos")
    // Exclude the unwanted dependencies
    api("org.jpos:jpos:1.9.6")
//   api('libs/urovosdkLibs-v62.aar')
//    {
//        exclude group: 'junit', module: 'junit'
//        exclude group: 'com.sleepycat', module: 'je'
//    }
    implementation(libs.dagger.hilt)
  //  implementation(libs.dagger.hilt.bom)
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
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
//    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("com.github.razaghimahdi:Compose-Persian-Date-Picker:1.1.1")
//    implementation("com.github.samanzamani:PersianDate:1.7.1")
    implementation("com.github.samanzamani:PersianDate:1.7.1")
   // implementation("com.github.samanzamani:PersianDate:1.7.1")
   // implementation("com.journeyapps:zxing-android-embedded:3.5.0")

}
//kapt {
//    correctErrorTypes = true
//}