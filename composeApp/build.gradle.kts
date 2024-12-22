import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}

val javafx_base_openapis = listOf(
    "com.sun.javafx.logging"
)

val javafx_graphics_openapis = listOf(
    "com.sun.javafx.application",
    "com.sun.javafx.embed",
    "com.sun.javafx.sg.prism",
    "com.sun.javafx.scene",
    "com.sun.javafx.util",
    "com.sun.prism",
    "com.sun.glass.ui",
    "com.sun.javafx.geom.transform",
    "com.sun.javafx.tk",
    "com.sun.glass.utils",
    "com.sun.javafx.font",
    "com.sun.javafx.scene.input",
    "com.sun.javafx.stage",
    "com.sun.javafx.geom",
    "com.sun.javafx.cursor",
    "com.sun.prism.paint",
    "com.sun.scenario.effect",
    "com.sun.javafx.text",
    "com.sun.javafx.scene.text",
    "com.sun.javafx.iio",
    "com.sun.scenario.effect.impl.prism"
)

val javafx_controls_openapis = listOf(
    "com.sun.javafx.scene.control"
)

val customJvmArgs = listOf(
    "--module-path",
    "/home/kylewong/Softwares/javafx-sdk-21.0.5/lib",
    "--add-modules",
    "javafx.controls,javafx.fxml"
) +
    (
        javafx_base_openapis.map { "javafx.base/" + it }
            + javafx_graphics_openapis.map { "javafx.graphics/" + it }
            + javafx_controls_openapis.map { "javafx.controls/" + it }
        ).map { it + "=ALL-UNNAMED" }.map { listOf("--add-opens", it) }.flatten()

tasks.withType<JavaExec> {
    jvmArgs(
        customJvmArgs
    )
}