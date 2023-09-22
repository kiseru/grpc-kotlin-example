plugins {
    kotlin("jvm") version "1.9.10"
    application
}

dependencies {
    implementation(project(":proto"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.grpc:grpc-stub:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-netty-shaded:${rootProject.ext["grpcVersion"]}")
    implementation("org.apache.commons:commons-lang3:3.4")
}

kotlin {
    jvmToolchain(17)
}
