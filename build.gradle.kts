    import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    `java-library`
}

group = "de.rebsc"
version = "0.0.1"

repositories {
    mavenCentral()
}


sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/kotlin"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources"))
        }
    }
    test {
        java {
            setSrcDirs(listOf("src/test/kotlin"))
        }
        resources {
            setSrcDirs(listOf("src/test/resources"))
        }
    }
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(group = "org.slf4j", name = "slf4j-api", version = "1.7.25")
    api(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")
    implementation(group = "org.opensourcebim", name = "ifcplugins", version = "0.0.99")
    implementation(group="org.locationtech.jts", name="jts-core", version="1.18.1")
    implementation("com.menecats:polybool-java:1.0.1")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-console:1.2.0")
}

tasks {
    // Use the native JUnit support of Gradle.
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}