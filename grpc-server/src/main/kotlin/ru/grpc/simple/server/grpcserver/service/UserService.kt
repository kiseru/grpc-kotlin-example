package ru.grpc.simple.server.grpcserver.service

import io.grpc.Status
import ru.grpc.simple.proto.CommonResponse
import ru.grpc.simple.proto.LoginRequest
import ru.grpc.simple.proto.LogoutRequest
import ru.grpc.simple.proto.UserServiceGrpcKt
import ru.grpc.simple.proto.commonResponse
import ru.grpc.simple.proto.util.DataUtil

private const val ONLINE = "ONLINE"

private const val OFFLINE = "OFFLINE"

class UserService : UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    override suspend fun login(request: LoginRequest): CommonResponse {
        if (request.password == DataUtil.getCorrectPassword()) {
            println("${request.username}'s login attempt has succeed")
            DataUtil.getUsersMap()[request.username] = ONLINE

            return commonResponse {
                responseCode = Status.Code.OK.value()
                responseMessage = "Login SUCCESS"
            }
        } else {
            println("${request.username}'s login attempt has failed")
            DataUtil.getUsersMap().remove(request.username)

            return commonResponse {
                responseCode = Status.Code.UNAUTHENTICATED.value()
                responseMessage = "Login FAILED"
            }
        }
    }

    override suspend fun logout(request: LogoutRequest): CommonResponse {
        val usersMap = DataUtil.getUsersMap()
        val status = usersMap[request.username]
        return if (status == null || status == OFFLINE) {
            commonResponse {
                responseCode = Status.Code.INVALID_ARGUMENT.value()
                responseMessage = "Logout FAILED, request is empty or user ${request.username} has been OFFLINE yet"
            }
        } else {
            usersMap[request.username] = OFFLINE
            commonResponse {
                responseCode = Status.Code.OK.value()
                responseMessage = "Logout SUCCESS for user: ${request.username}"
            }
        }
    }
}