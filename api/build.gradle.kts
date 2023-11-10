plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
}

group = "pl.ejdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("com.beust:klaxon:5.6")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}