package de.ckthomas.smart.iot

import de.ckthomas.smart.iot.services.MqttCallbackService
import de.ckthomas.smart.iot.services.SmartIotBootstrapService
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.core.Ordered
import java.util.*

import javax.annotation.PostConstruct

/**
 * Author: Christian Thomas
 * Created on: 30.11.2021
 *
 * Check license details @ project root
 *
 * It is relevant to provide some spring components before camunda is booting up...
 *
 * E.g. the camunda connectors, that are NOT instantiated in a spring context!
 *
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
class SpringConfig {

    private val logger = logFor(SpringConfig::class.java)

    @PostConstruct
    fun init() {
        logger.info("C A M U N D A - S M A R T - I O T # <SPRING-CONFIG> IS - B O O T I N G - U P ...")
    }

    @Configuration
    @ComponentScan(basePackages = [
        "de.ckthomas.smart.iot.components",
        "de.ckthomas.smart.iot.services"
    ])
    class ComponentScanConfiguration {}

    data class HassioData(val authKey: String, val authValue: String, val basePath: String)

    @Configuration
    class HassioConfiguration {

        @Value("\${iot.hassio.auth-key}")
        lateinit var authKey: String

        @Value("\${iot.hassio.auth-value}")
        lateinit var authValue: String

        @Value("\${iot.hassio.base-path}")
        lateinit var basePath: String

        @Bean
        fun createHassioConfigData() = HassioData(authKey, authValue, basePath)
    }

    data class MqttData(val brokerUrl: String, val username: String, val password: String)

    @Configuration
    class MqttConfiguration {

        @Value("\${iot.mqtt.broker-url}")
        lateinit var brokerUrl: String

        @Value("\${iot.mqtt.username}")
        lateinit var username: String

        @Value("\${iot.mqtt.password}")
        lateinit var password: String

        @Bean
        fun createMqttConfigData() = MqttData(brokerUrl, username, password)

        @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
        @Bean
        fun createMqttCallbackService(): MqttCallbackService {
            val mqttData = createMqttConfigData()

            val options = MqttConnectOptions()
            options.userName = mqttData.username
            options.password = mqttData.password.toCharArray()

            options.isAutomaticReconnect = true
            options.isCleanSession = true

            val uuid = "IOT_${UUID.randomUUID().toString()}"
            val client = MqttAsyncClient(mqttData.brokerUrl, uuid)
            client.connect(options)

            val mqttService = MqttCallbackService(client)
            client.setCallback(mqttService)

            return mqttService
        }
    }
}

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
class CamundaConfig(private val smartIotBootstrapService: SmartIotBootstrapService) {

    private val logger = logFor(CamundaConfig::class.java)

    @PostConstruct
    fun init() {
        try {
            logger.info("C A M U N D A - S M A R T - I O T # <CAMUNDA-CONFIG> IS - B O O T I N G - U P ...")
            smartIotBootstrapService.bootstrapTopics()
        } catch (t: Throwable) {
            logger.error("Something went wrong during init process! Check cause statement!", t)
        }
    }

    @Configuration
    @ComponentScan(basePackages = [
        "de.ckthomas.smart.iot.camunda.plugins",
        "de.ckthomas.smart.iot.camunda.listeners"
    ])
    class ComponentScanConfiguration {}

}