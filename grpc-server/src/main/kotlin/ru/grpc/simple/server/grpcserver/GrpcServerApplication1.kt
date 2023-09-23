package ru.grpc.simple.server.grpcserver

import io.grpc.Metadata
import io.grpc.ServerBuilder
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import ru.grpc.simple.server.grpcserver.service.UserService

fun main() {
    val server = ServerBuilder.forPort(9091)
        .addService(UserService())
        .intercept(ApplicationInterceptor())
        .build()

    server.start()

    println("gRPC Server starter on port: ${server.port}")

    server.awaitTermination()
}

class ApplicationInterceptor : ServerInterceptor {

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        println("Received following metadata: $headers")
        return next.startCall(call, headers)
    }
}