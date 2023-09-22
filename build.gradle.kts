plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("com.google.protobuf") version "0.9.4" apply false
}

repositories {
    mavenLocal()
    mavenCentral()
}

ext["grpcVersion"] = "1.58.0"
ext["protobufVersion"] = "3.24.0"
ext["protocVersion"] = ext["protobufVersion"]
ext["grpcKotlinVersion"] = "1.3.1"
ext["coroutinesVersion"] = "1.7.3"

subprojects {
    repositories {
        mavenCentral()
    }
}
