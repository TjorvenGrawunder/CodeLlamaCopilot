plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("com.teamdev.jxbrowser") version "1.1.0"
    id("org.jetbrains.intellij.platform") version "2.0.1"
    //id("org.jetbrains.intellij.platform.migration") version "2.0.1"

}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    intellijPlatform {
        defaultRepositories()
    }
    mavenCentral()
}


jxbrowser {
    version = "7.40.0"
}

intellijPlatform {
    buildSearchableOptions = true
    instrumentCode = true
    projectName = project.name

    pluginConfiguration {
        id = "lllm-copilot"
        name = "Large Language Model Copilot"
        version = "1.0.0"
        description = "This Plugin is a copilot for various large language models!"

        ideaVersion {
            sinceBuild = "241"
            untilBuild = "242.*"
        }
    }
    publishing {
        // ...
    }
    signing {
        // ...
    }
    pluginVerification {
        // ...
    }
}


dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.0.1")
        bundledPlugin("com.intellij.java")
        instrumentationTools()
    }
    // https://mvnrepository.com/artifact/com.fifesoft/rsyntaxtextarea
    implementation("com.fifesoft:rsyntaxtextarea:3.0.4")
    // https://central.sonatype.com/artifact/es.nitaur.markdown/txtmark
    implementation("es.nitaur.markdown:txtmark:0.16")
    // https://mvnrepository.com/artifact/com.vladsch.flexmark/flexmark
    implementation("com.vladsch.flexmark:flexmark:0.9.1")
    implementation(jxbrowser.core)
    implementation(jxbrowser.crossPlatform)
    implementation(jxbrowser.swing)
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
/*intellij {
    version.set("2023.2.5")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}*/

/*tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}*/
