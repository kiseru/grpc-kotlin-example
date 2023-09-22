plugins {
    id("com.google.protobuf") version "0.9.4" apply false
}

repositories {
    mavenCentral()
}

ext["grpcVersion"] = "1.58.0"
ext["protobufVersion"] = "3.24.0"
ext["protocVersion"] = ext["protobufVersion"]

subprojects {
    repositories {
        mavenCentral()
    }
}
