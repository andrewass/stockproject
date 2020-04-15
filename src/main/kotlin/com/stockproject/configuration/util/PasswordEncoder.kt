package com.stockproject.configuration.util

import java.security.MessageDigest


class PasswordEncoder{

    fun encode(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByteArray())
        return String(messageDigest.digest())
    }

    fun matches(submitted: String, stored: String): Boolean {
        val encoded = encode(submitted)
        return encoded == stored
    }
}