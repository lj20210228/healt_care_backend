plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}
tasks.test{
    useJUnitPlatform()
}
dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.24") // ili verzija koja ide uz tvoj Kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.24")
    // Mockito

// Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")


    testImplementation("io.mockk:mockk:1.13.9")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")


    implementation("org.jetbrains.exposed:exposed-core:0.57.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.57.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.57.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.57.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")

    implementation("com.mysql:mysql-connector-j:8.4.0")
    implementation("at.favre.lib:bcrypt:0.10.2")

}
