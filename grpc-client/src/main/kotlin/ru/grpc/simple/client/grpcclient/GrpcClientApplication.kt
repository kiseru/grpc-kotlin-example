package ru.grpc.simple.client.grpcclient

import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.grpc.simple.proto.CommonResponse
import ru.grpc.simple.proto.LoginRequest
import ru.grpc.simple.proto.LogoutRequest
import ru.grpc.simple.proto.UserGrpc

suspend fun main() {
    val metadata = Metadata()
    metadata.put(Metadata.Key.of("Client-Header", Metadata.ASCII_STRING_MARSHALLER), "Some cool header")
    val interceptor = MetadataUtils.newAttachHeadersInterceptor(metadata)
    val managedChannel = ManagedChannelBuilder.forAddress("0.0.0.0", 9091)
        .usePlaintext()
        .intercept(interceptor)
        .build()

    val stub = UserGrpc.newStub(managedChannel)

    val loginRequest = LoginRequest.newBuilder()
        .setUsername("userTestLogin")
        .setPassword("qwe123")
        .build()
    login(stub, loginRequest)
        .collect {
            println("Login -> Response code: ${it.responseCode}, message: ${it.responseMessage}")
            println("Login -> All fields: ${it.allFields}")
        }

    val logoutRequest = LogoutRequest.newBuilder()
        .setUsername("userTestLogin")
        .build()
    logout(stub, logoutRequest)
        .collect {
            println("Logout -> Response code: ${it.responseCode}, message: ${it.responseMessage}")
            println("Logout -> All fields: ${it.allFields}")
        }
}

suspend fun login(stub: UserGrpc.UserStub, loginRequest: LoginRequest): Flow<CommonResponse> =
    callbackFlow {
        stub.login(loginRequest, object : StreamObserver<CommonResponse> {

            override fun onNext(value: CommonResponse) {
                trySend(value)
            }

            override fun onError(t: Throwable) {
                cancel(CancellationException("Error occurred", t))
            }

            override fun onCompleted() {
                channel.close()
            }
        })

        awaitClose()
    }

suspend fun logout(stub: UserGrpc.UserStub, logoutRequest: LogoutRequest): Flow<CommonResponse> =
    callbackFlow {
        stub.logout(logoutRequest, object : StreamObserver<CommonResponse> {

            override fun onNext(value: CommonResponse) {
                trySend(value)
            }

            override fun onError(t: Throwable) {
                cancel(CancellationException("Error occurred", t))
            }

            override fun onCompleted() {
                channel.close()
            }
        })

        awaitClose()
    }
