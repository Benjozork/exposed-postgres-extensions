plugins {
    kotlin("jvm") version "1.3.72"
    maven
}

group = "dev.31416"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback", "logback-classic", "1.2.3")

    implementation("org.postgresql", "postgresql", "42.1.4")
    implementation("org.jetbrains.exposed", "exposed-core", "0.23.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.23.1")

    testImplementation("org.junit.jupiter", "junit-jupiter", "5.5.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType(Test::class) {
        useJUnitPlatform()
    }
}
