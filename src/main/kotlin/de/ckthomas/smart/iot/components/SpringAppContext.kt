package de.ckthomas.smart.iot.components

import de.ckthomas.smart.iot.exceptions.SmartIotException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.jvm.Throws

/**
 * Author: Christian Thomas
 * Created on: 30.11.2021
 *
 * Is useful to get access to spring components/beans in a non spring class context.
 *
 * Check license details @ project root
 */
@Component
class SpringAppContext : ApplicationContextAware {

    companion object {
        var context: ApplicationContext? = null

        @Throws(SmartIotException::class)
        fun <T>getBean(beanClass: Class<T>): T {
            val appContext = context ?: throw SmartIotException("No spring application context is given!")
            return appContext.getBean(beanClass)
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

}