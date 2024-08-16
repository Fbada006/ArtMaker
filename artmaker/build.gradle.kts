/*
 * Copyright 2024 ArtMaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.spotless)
    alias(libs.plugins.vanniktech)
}

apply(from = "${rootDir}/scripts/publish.gradle.kts")

mavenPublishing {
    val artifactId = "artmaker"

    coordinates(
        "io.github.fbada006",
        artifactId,
        rootProject.extra.get("libVersion").toString()
    )

    pom {
        name.set(artifactId)
        description.set("ArtMaker is a flexible and customisable library that allows users to draw anything they want on screen and has been built fully with Jetpack Compose.")
        url.set("https://github.com/fbada006/artmaker/")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("Fbada006")
                name.set("Ferdinand Bada")
                url.set("https://github.com/Fbada006/")
            }
            developer {
                id.set("CalebKL")
                name.set("Caleb Langat")
                url.set("https://github.com/CalebKL/")
            }
            developer {
                id.set("EmmanuelMuturia")
                name.set("Emmanuel Muturia")
                url.set("https://github.com/EmmanuelMuturia/")
            }
        }
    }
}

android {
    namespace = "io.fbada006.artmaker"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
  buildFeatures {
    compose = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = libs.versions.jvmTarget.get()
  }
}

dependencies {
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime.compose)
    implementation (libs.material.icons)
    implementation(libs.photo.picker)
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui:1.7.0-beta06") {
        because("We need to use graphics layer to export composable as image.")
    }
    implementation (libs.accompanist.permissions)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}