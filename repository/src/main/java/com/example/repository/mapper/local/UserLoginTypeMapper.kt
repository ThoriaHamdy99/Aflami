package com.example.repository.mapper.local

import com.example.domain.utils.SessionType

class UserLoginTypeMapper {
    fun toLocalLoginType(loginType: SessionType): String{
        return when(loginType){
            SessionType.NOT_LOGGED_IN -> NONE
            SessionType.LOGGED_IN -> USER
            SessionType.GUEST -> GUEST
        }
    }

    fun fromLocalLoginType(loginType: String): SessionType{
        return when(loginType){
            USER -> SessionType.LOGGED_IN
            GUEST -> SessionType.GUEST
            else -> SessionType.NOT_LOGGED_IN
        }
    }

    private companion object{
        private const val GUEST = "GUEST"
        private const val USER = "USER"
        private const val NONE = "NONE"
    }
}