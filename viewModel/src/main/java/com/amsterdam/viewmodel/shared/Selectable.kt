package com.amsterdam.viewmodel.shared

data class Selectable<T>(
    val isSelected: Boolean,
    val item: T
)
