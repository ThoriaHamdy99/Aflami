package com.example.localdatasource.dataStore.utils

interface CryptoData {
    fun encryptString(input: String): String

    fun decryptString(input: String): String?
}
