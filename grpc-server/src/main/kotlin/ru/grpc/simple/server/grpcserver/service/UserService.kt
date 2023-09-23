package ru.grpc.simple.server.grpcserver.service

import io.grpc.Status
import ru.grpc.simple.proto.CommonResponse
import ru.grpc.simple.proto.LoginRequest
import ru.grpc.simple.proto.LogoutRequest
import ru.grpc.simple.proto.UserServiceGrpcKt
import ru.grpc.simple.proto.commonResponse
import ru.grpc.simple.server.grpcserver.repository.UserRepository
import ru.grpc.simple.server.grpcserver.repository.UserStatus

class UserService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    override suspend fun login(request: LoginRequest): CommonResponse =
        if (UserRepository.checkPassword(request.password)) {
            println("${request.username}'s login attempt has succeed")
            UserRepository.setOnline(request.username)
            commonResponse {
                responseCode = Status.Code.OK.value()
                responseMessage = "Login SUCCESS"
            }
        } else {
            println("${request.username}'s login attempt has failed")
            UserRepository.remove(request.username)
            commonResponse {
                responseCode = Status.Code.UNAUTHENTICATED.value()
                responseMessage = "Login FAILED"
            }
        }

    override suspend fun logout(request: LogoutRequest): CommonResponse =
        if (UserRepository.getUserStatus(request.username) != UserStatus.ONLINE) {
            commonResponse {
                responseCode = Status.Code.INVALID_ARGUMENT.value()
                responseMessage = "Logout FAILED, request is empty or user ${request.username} has been OFFLINE yet"
            }
        } else {
            UserRepository.setOffline(request.username)
            commonResponse {
                responseCode = Status.Code.OK.value()
                responseMessage = "Logout SUCCESS for user: ${request.username}"
            }
        }
}