plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":proto"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.ext["coroutinesVersion"]}")
    implementation("io.grpc:grpc-stub:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-kotlin-stub:${rootProject.ext["grpcKotlinVersion"]}")
    implementation("io.grpc:grpc-protobuf:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-netty-shaded:${rootProject.ext["grpcVersion"]}")
    implementation("org.apache.commons:commons-lang3:3.4")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
}

kotlin {
    jvmToolchain(17)
}
