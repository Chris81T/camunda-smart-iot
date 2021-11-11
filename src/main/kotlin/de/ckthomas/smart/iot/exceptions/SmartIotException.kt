package de.ckthomas.smart.iot.exceptions

import java.lang.RuntimeException

/**
 * Author: Christian Thomas
 * Created on: 11.11.2021
 *
 * Check license details @ project root
 */
class SmartIotException(message: String, cause: Exception? = null) : RuntimeException(message, cause) {}