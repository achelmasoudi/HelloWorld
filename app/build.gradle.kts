plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // for Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.achelm.helloworld"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.achelm.helloworld"
        minSdk = 23
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Glide Lib for loading images from url
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Cicle Image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Lottie Lib for Animation
    implementation("com.airbnb.android:lottie:6.1.0")

    // For Image Picker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    // FireBase Lib
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")

    // To enable zoom in and out with fingers for an ImageView
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // For Json File ( Languages Selection from Json )
    implementation("com.google.code.gson:gson:2.10.1")

    // Country Selection Lib
    implementation("com.hbb20:ccp:2.7.3")

}