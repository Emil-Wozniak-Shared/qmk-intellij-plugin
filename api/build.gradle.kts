plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

group = "pl.ejdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    implementation("commons-codec:commons-codec:1.16.0")
    implementation("com.beust:klaxon:5.6")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}