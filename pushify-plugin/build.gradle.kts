plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.fotsapp"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

// Configure Gradle IntelliJ Plugin
intellij {
    version.set("2023.2.6") // Target IDE version (Android Studio Hedgehog/Iguana based on this)
    type.set("IC") // IntelliJ Community Edition
    
    // "com.intellij.java" is bundled with IC, no need to list it explicitly in newer versions unless needed.
    // However, the error "Cannot find builtin plugin" suggests it's failing to resolve.
    // Let's try removing it from here and rely on implicit dependencies or just 'java' if needed.
    // For simple tool windows, usually no explicit plugin dependency is needed unless you touch PSI.
    plugins.set(listOf()) 
}

tasks {
    // Set the JVM compatibility for the plugin
    // Commented out explicit jvmTarget to let toolchain handle it
    /*
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    */

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("253.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

