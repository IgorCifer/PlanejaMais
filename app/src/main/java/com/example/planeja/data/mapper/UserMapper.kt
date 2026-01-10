package com.example.planeja.data.mapper

import com.example.planeja.data.local.entity.UserEntity
import com.example.planeja.domain.model.User

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        email = this.email,
        name = this.name,
        createdAt = this.createdAt
    )
}

fun User.toUserEntity(passwordHash: String): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        passwordHash = passwordHash,
        name = this.name,
        createdAt = this.createdAt
    )
}
