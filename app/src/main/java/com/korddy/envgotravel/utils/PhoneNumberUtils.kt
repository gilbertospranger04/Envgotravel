package com.korddy.envgotravel.utils

object PhoneNumberUtils {

 
    fun formatForPost(raw: String): String {
        return raw.replace(" ", "")
    }

    fun formatForGet(raw: String, countryCode: String): String {
        val digits = raw.filter { it.isDigit() }
        return if (digits.startsWith(countryCode)) {
            val number = digits.removePrefix(countryCode)
            "+$countryCode $number"
        } else {
            "+$countryCode $digits"
        }
    }
}
