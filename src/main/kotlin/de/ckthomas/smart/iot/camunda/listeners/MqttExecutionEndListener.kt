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
class MqttExecutionEndListener() : AbstractMqttExecutionListener(MqttExecutionEndListener::class.java) {

    override fun notify(execution: DelegateExecution?) {
        val processInstanceId = execution?.processInstanceId
        val topic = getSignalName(execution!!)

        LOG.info("About to stop listening to Mqtt topic = {}, ProcessInstanceId = {}", topic, processInstanceId)

        LOG.warn("TODO - Not yet implemented!")
    }
}