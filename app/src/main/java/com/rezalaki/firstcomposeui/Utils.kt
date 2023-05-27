package com.rezalaki.firstcomposeui

import java.util.regex.Pattern

object Utils {

    fun isValidPhone(phoneString: String): Boolean {
        if (phoneString.isEmpty())
            return false

        return Pattern.compile("^09\\d{9}\$").matcher(phoneString).find()
    }

}