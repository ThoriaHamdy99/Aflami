package com.example.repository.mapper.local

import com.example.domain.utils.UserLoginType

class UserLoginTypeMapper {
    fun toLocalLoginType(loginType: UserLoginType): String{
        return when(loginType){
            UserLoginType.NONE -> NONE
            UserLoginType.USER -> USER
            UserLoginType.GUEST -> GUEST
        }
    }

    fun fromLocalLoginType(loginType: String): UserLoginType{
        return when(loginType){
            USER -> UserLoginType.USER
            GUEST -> UserLoginType.GUEST
            else -> UserLoginType.NONE
        }
    }

    private companion object{
        private const val GUEST = "GUEST"
        private const val USER = "USER"
        private const val NONE = "NONE"
    }
}