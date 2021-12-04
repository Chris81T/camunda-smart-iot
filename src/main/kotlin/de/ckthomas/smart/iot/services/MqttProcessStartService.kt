package de.ckthomas.smart.iot.services

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.logFor
import org.camunda.bpm.engine.DecisionService
import org.camunda.bpm.engine.ProcessEngineException
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.variable.Variables
import org.springframework.stereotype.Service

/**
 * Author: Christian Thomas
 * Created on: 02.12.2021
 *
 * Check license details @ project root
 */
@Service
class MqttProcessStartService(
    private val repositoryService: RepositoryService,
    private val decisionService: DecisionService) {

    private val logger = logFor(MqttProcessStartService::class.java)

    fun bootstrapTopics() {
        logger.info("About to boostrap topics...")

        val setupDecision = repositoryService.createDecisionDefinitionQuery()
            .decisionDefinitionKey(IotConstants.Setup.DMN_SETUP_DECISION_KEY)
            .singleResult()

        if (setupDecision != null) {
            try {
                logger.info("Found a Setup-Decision. About to evaluate it...")
                val result = decisionService.evaluateDecisionTableByKey(IotConstants.Setup.DMN_SETUP_DECISION_KEY)
                    .variables(Variables.createVariables()
                        .putValue(IotConstants.Setup.DMN_SETUP_DECISION_INPUT_NAME, IotConstants.Setup.DMN_MQTT_PROCESS_START_TOPIC))
                    .evaluate()

                logger.info("Got following result = {}", result)

                val outputValues = result.collectEntries<String>(IotConstants.Setup.DMN_SETUP_DECISION_OUTPUT_NAME)

                logger.info("Found following output values = {} to setup the startup subscriptions (mqtt -> process start)",
                    outputValues)

            } catch (e: ProcessEngineException) {
                logger.error("Failed to evaluate setup decision! Check the cause statement!", e)
            } catch (t: Throwable) {
                logger.error("Something went wrong during bootstrap! Check the cause statement!", t)
            }
        }
    }

}