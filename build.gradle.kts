plugins {
	alias(libs.plugins.kotlin.compose) apply false
	alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
	alias(libs.plugins.android.kotlin.multiplatform.library) apply false
	alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
}

extra["compileSdk"] = 36
extra["targetSdk"] = 36
extra["minSdk"] = 24
extra["javaVersion"] = JavaVersion.VERSION_11