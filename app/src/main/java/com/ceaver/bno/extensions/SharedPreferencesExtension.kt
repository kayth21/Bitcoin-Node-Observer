package com.ceaver.bno.extensions

import android.content.SharedPreferences

fun SharedPreferences.getInt(key: String): Int? {
    val value = getInt(key, Int.MIN_VALUE)
    return if (value == Int.MIN_VALUE) null else value
}

fun SharedPreferences.getLong(key: String): Long? {
    val value = getLong(key, Long.MIN_VALUE)
    return if (value == Long.MIN_VALUE) null else value
}

fun SharedPreferences.getString(key: String): String? {
    val value = getString(key, "")
    return if (value == "") null else value
}