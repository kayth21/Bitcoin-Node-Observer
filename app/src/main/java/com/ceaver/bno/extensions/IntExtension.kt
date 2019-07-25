package com.ceaver.bno.extensions

import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

fun Int.asLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(toLong()),
        TimeZone.getDefault().toZoneId()
    )
}

fun Int.asFormattedNumber(): String {
    val formatter = DecimalFormat("#,###,###")
    return formatter.format(this)
}