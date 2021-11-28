package de.ckthomas.smart.iot

import org.springframework.beans.factory.annotation.Value
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
 * Example how to override application properties while using docker-compose (etc):
 *     https://www.tutorialworks.com/spring-boot-kubernetes-override-properties/#:~:text=To%20override%20your%20Spring%20Boot%20application%20properties%20when,the%20environment%20variables%20that%20you%20want%20to%20override.
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

    @Configuration
    class MqttConfiguration {

        @Value("\${mqtt.broker}")
        lateinit var brokerUrl: String

        @Value("\${mqtt.username}")
        lateinit var username: String

        @Value("\${mqtt.password}")
        lateinit var password: String


    }
}
