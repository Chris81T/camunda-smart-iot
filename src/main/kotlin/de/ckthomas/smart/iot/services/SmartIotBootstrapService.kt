package de.ckthomas.smart.iot.services

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.logFor
import org.camunda.bpm.engine.DecisionService
import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.variable.Variables
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Author: Christian Thomas
 * Created on: 02.12.2021
 *
 * Check license details @ project root
 */
@Service
class SmartIotBootstrapService(
    private val mqttService: MqttCallbackService,
    private val repositoryService: RepositoryService,
    private val runtimeService: RuntimeService,
    private val decisionService: DecisionService) {

    private val logger = logFor(SmartIotBootstrapService::class.java)

    @Throws(Exception::class)
    private fun evaluateSetupDmn(): List<String> {
        val result = decisionService.evaluateDecisionTableByKey(IotConstants.Setup.DMN_SETUP_DECISION_KEY)
            .variables(Variables.createVariables()
                .putValue(IotConstants.Setup.DMN_SETUP_DECISION_INPUT_NAME, IotConstants.Setup.DMN_MQTT_PROCESS_START_TOPIC))
            .evaluate()

        return result.collectEntries<String>(IotConstants.Setup.DMN_SETUP_DECISION_OUTPUT_NAME)
    }

    @Throws(Exception::class)
    private fun subscribeProcessStartTopics(mqttTopics: List<String>) {
        val subscriptionIds = mqttTopics.map {
            val subscription = MqttSubscription(
                it,
                { topic, message ->
                    logger.info("Incoming message for topic = {}. Message = {}", topic, message.payload)
                    // TODO runtimeService.signal
                }
            )

            mqttService.subscribe(subscription)
        }

        logger.info("Got following subscriptionIds = {}", subscriptionIds)
    }

    fun bootstrapTopics() {
        logger.info("About to boostrap topics...")

        val setupDecision = repositoryService.createDecisionDefinitionQuery()
            .decisionDefinitionKey(IotConstants.Setup.DMN_SETUP_DECISION_KEY)
            .singleResult()

        if (setupDecision != null) {
            try {
                logger.info("Found a Setup-Decision. About to evaluate it...")

                val mqttStartProcessTopics = evaluateSetupDmn()
                logger.info("Found following mqttStartProcessTopics = {}. About to subscribe them...",
                    mqttStartProcessTopics)

                subscribeProcessStartTopics(mqttStartProcessTopics)

            } catch (e: ProcessEngineException) {
                logger.error("Failed to evaluate setup decision! Check the cause statement!", e)
            } catch (t: Throwable) {
                logger.error("Something went wrong during bootstrap! Check the cause statement!", t)
            }
        }
    }

}