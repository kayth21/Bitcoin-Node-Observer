package com.ceaver.bno.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.util.*

fun Int.asLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(toLong()),
        TimeZone.getDefault().toZoneId()
    )
}