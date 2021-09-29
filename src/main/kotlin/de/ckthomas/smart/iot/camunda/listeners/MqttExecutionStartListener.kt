package de.ckthomas.smart.iot.camunda.listeners

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.stereotype.Component

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
@Component
class MqttExecutionStartListener() : AbstractMqttExecutionListener(MqttExecutionStartListener::class.java) {

    override fun notify(execution: DelegateExecution?) {
        val processInstanceId = execution?.processInstanceId
        val topic = getSignalName(execution!!)
        val resultVariable = getResultVariableName(execution)

        LOG.info("About to start listening over factory to Mqtt topic = {}, ProcessInstanceId = {}, " +
                    "resultVariable = {}", topic, processInstanceId, resultVariable)

        LOG.warn("TODO - Not yet implemented!")
    }
}