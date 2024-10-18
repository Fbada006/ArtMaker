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
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}
//https://youtrack.jetbrains.com/issue/KT-66448/Multiplatform-wizards.-Get-rid-of-deprecated-kotlinOptions
@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            with(compose) {
                implementation(ui)
                implementation(runtime)
                implementation(foundation)
                implementation(materialIconsExtended)
                implementation(material3)
            }
            api(libs.moko.permissions)
            api(libs.moko.permissions.compose)
            implementation(compose.components.resources)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.kotlinx.serialization.json)
            api(libs.androidx.datastore.preferences.core)
            api(libs.androidx.datastore)
            implementation(project(":customcolorpalette"))
        }
        commonTest.dependencies {
        }
        androidMain.dependencies {
            implementation("androidx.compose.ui:ui:1.7.4") {
                because("We need to use graphics layer to export composable as image.")
            }
            implementation (libs.accompanist.permissions)
            implementation("androidx.appcompat:appcompat:1.7.0")
            implementation("androidx.activity:activity-compose:1.9.2")
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "io.fbada006.artmaker"
    generateResClass = auto
}

android {
    namespace = "com.fbada006.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
