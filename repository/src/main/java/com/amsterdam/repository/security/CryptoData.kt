package com.amsterdam.repository.security

interface CryptoData {
    fun encrypt(bytes: ByteArray): ByteArray

    fun decrypt(bytes: ByteArray): ByteArray?

    fun encryptString(input: String): String

    fun decryptString(input: String): String?
}
