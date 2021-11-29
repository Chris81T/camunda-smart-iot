package de.ckthomas.smart.iot

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Helpful function to create a specific logging instance
 */
fun <T> logFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

fun <T> multiLet(valuesToCheck: List<T?>): Boolean {
    return valuesToCheck.all { value -> value?.let { true } ?: false }
}