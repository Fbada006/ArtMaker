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
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.vanniktech)
}

apply(from = "${rootDir}/scripts/publish.gradle.kts")

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    val artifactId = "artmaker"

    coordinates(
        "io.github.fbada006",
        artifactId,
        rootProject.extra.get("libVersion").toString(),
    )

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        ),
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
        scm {
            url.set("https://github.com/fbada006/artmaker/")
            connection.set("scm:git:git://github.com/fbada006/artmaker.git")
            developerConnection.set("scm:git:ssh://git@github.com/fbada006/artmaker.git")
        }
    }
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
            baseName = "artmaker"
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
            implementation(compose.components.resources)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.kotlinx.serialization.json)
            api(libs.androidx.datastore.preferences.core)
            api(libs.androidx.datastore)
            implementation(libs.colormath)
        }
        commonTest.dependencies {
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "io.fbada006.artmaker"
    generateResClass = auto
}

android {
    namespace = "io.fbada006.artmaker"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
