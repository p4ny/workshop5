val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies { // <--- บล็อก dependencies เริ่มต้นที่นี่

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    // *** ต้องย้ายมาไว้ในบล็อก dependencies ***
    implementation("io.ktor:ktor-server-config-yaml") // <-- ย้ายมาที่นี่

    implementation("ch.qos.logback:logback-classic:1.4.14")

    // สำหรับ database interaction (ตัวอย่างเช่น Exposed, JDBC driver)
    // โปรดเลือกและเปิดใช้งานตามที่คุณต้องการใช้
    // implementation("org.jetbrains.exposed:exposed-core:0.49.0")
    // implementation("org.jetbrains.exposed:exposed-dao:0.49.0")
    // implementation("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    // implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0-M1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0-M1")
} // <--- บล็อก dependencies สิ้นสุดที่นี่