package ru.grpc.simple.server.grpcserver.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object UserRepository {

    private val correctPassword = "qwe123"

    private val mutex = Mutex()

    private val users = mutableMapOf<String, UserStatus>()

    suspend fun getUserStatus(username: String): UserStatus? =
        mutex.withLock {
            users[username]
        }

    fun checkPassword(password: String): Boolean =
        password == correctPassword

    suspend fun setOnline(username: String) =
        mutex.withLock {
            users[username] = UserStatus.ONLINE
        }

    suspend fun setOffline(username: String) =
        mutex.withLock {
            users[username] = UserStatus.OFFLINE
        }

    suspend fun remove(username: String) =
        mutex.withLock {
            users.remove(username)
        }
}

