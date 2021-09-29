package de.ckthomas.smart.iot

import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import javax.annotation.PostConstruct

/**
 * Author: Christian Thomas
 * Created on: 27.09.2021
 *
 * Check license details @ project root
 *
 * Ordered.LOWEST_PRECEDENCE -> this is configured last
 * @ConditionalOnBean -> perform this here only, if the process-engine exists
 *
 */
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnBean(type = ["org.camunda.bpm.engine.ProcessEngine"])
class SpringBootConfig {

    private val LOG = logFor(SpringBootConfig::class.java)

    @PostConstruct
    fun init() {
       LOG.info("C A M U N D A - S M A R T - I O T # IS - B O O T I N G - U P ...")
    }

    @Configuration
    @ComponentScan(basePackages = ["de.ckthomas.smart.iot.camunda.plugins", "de.ckthomas.smart.iot.camunda.listeners"])
    class ComponentScanConfiguration {}
}
