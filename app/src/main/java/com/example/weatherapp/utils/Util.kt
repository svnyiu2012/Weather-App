package com.example.weatherapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Util {
    @JvmStatic
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}