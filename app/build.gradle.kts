import java.util.Properties

val localSecrets = Properties().apply {
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        secretsFile.inputStream().use(::load)
    }
}

fun secretValue(name: String): String =
    (localSecrets.getProperty(name) ?: System.getenv(name).orEmpty())
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.github.himath2002.mealmetric"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.himath2002.mealmetric"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1.1"

        buildConfigField("String", "NUTRITIONIX_APP_ID", "\"${secretValue("NUTRITIONIX_APP_ID")}\"")
        buildConfigField("String", "NUTRITIONIX_APP_KEY", "\"${secretValue("NUTRITIONIX_APP_KEY")}\"")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    lint {
        lintConfig = file("lint.xml")
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.runtime)
    implementation(libs.material)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    annotationProcessor(libs.androidx.room.compiler)
}
