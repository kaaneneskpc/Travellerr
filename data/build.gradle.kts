plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
}

kotlin {
    androidLibrary {
        namespace = "com.kaaneneskpc.data"
        compileSdk = 36
        minSdk = 24
        withHostTestBuilder {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    val xcfName = "dataKit"
    iosX64 {
        binaries.framework { baseName = xcfName }
    }
    iosArm64 {
        binaries.framework { baseName = xcfName }
    }
    iosSimulatorArm64 {
        binaries.framework { baseName = xcfName }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)
                implementation(project(":domain"))
            }
        }
    }
}
