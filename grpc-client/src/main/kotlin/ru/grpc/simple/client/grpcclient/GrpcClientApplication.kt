package ru.grpc.simple.client.grpcclient

import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import ru.grpc.simple.proto.UserServiceGrpcKt
import ru.grpc.simple.proto.loginRequest
import ru.grpc.simple.proto.logoutRequest

suspend fun main() {
    val metadata = Metadata()
    metadata.put(Metadata.Key.of("Client-Header", Metadata.ASCII_STRING_MARSHALLER), "Some cool header")
    val interceptor = MetadataUtils.newAttachHeadersInterceptor(metadata)
    val managedChannel = ManagedChannelBuilder.forAddress("0.0.0.0", 9091)
        .usePlaintext()
        .intercept(interceptor)
        .build()

    val stub = UserServiceGrpcKt.UserServiceCoroutineStub(managedChannel)

    val loginRequest = loginRequest {
        username = "userTestLogin"
        password = "qwe123"
    }
    val loginResponse = stub.login(loginRequest)
    println("Login -> Response code: ${loginResponse.responseCode}, message: ${loginResponse.responseMessage}")
    println("Login -> All fields: ${loginResponse.allFields}")

    val logoutRequest = logoutRequest {
        username = "userTestLogin"
    }
    val logoutResponse = stub.logout(logoutRequest)
    println("Logout -> Response code: ${logoutResponse.responseCode}, message: ${logoutResponse.responseMessage}")
    println("Logout -> All fields: ${logoutResponse.allFields}")
}
