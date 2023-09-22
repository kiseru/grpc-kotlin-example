plugins {
    application
}

dependencies {
    implementation(project(":proto"))
    implementation("io.grpc:grpc-stub:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-netty-shaded:${rootProject.ext["grpcVersion"]}")
    implementation("org.apache.commons:commons-lang3:3.4")
}
