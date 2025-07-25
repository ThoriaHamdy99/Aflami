package com.amsterdam.ui.screens.login.components

import com.amsterdam.designsystem.R

fun getPasswordTextFieldIcon(isPasswordShown: Boolean): Int{
    return if (isPasswordShown) R.drawable.ic_password_show else R.drawable.ic_password_hide
}