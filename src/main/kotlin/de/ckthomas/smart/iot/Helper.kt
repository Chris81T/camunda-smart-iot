package de.ckthomas.smart.iot

import org.slf4j.LoggerFactory

/**
 * Helpful function to create a specific logging instance
 */
fun <T> logFor(clazz: Class<T>) = LoggerFactory.getLogger(clazz)