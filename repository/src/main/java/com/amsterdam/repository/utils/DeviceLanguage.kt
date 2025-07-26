package com.amsterdam.repository.utils

import java.util.Locale

fun getDeviceLanguage(): String {
    return Locale.getDefault().language
}