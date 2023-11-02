package com.catnip.foodfood.utils

import java.text.NumberFormat
import java.util.Locale

fun Int.toCurrencyFormat():String{
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}