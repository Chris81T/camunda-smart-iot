package de.ckthomas.smart.iot.camunda.listeners

import org.camunda.bpm.engine.delegate.DelegateExecution

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
class MqttExecutionEndListener(signalRef: String) : AbstractMqttExecutionListener(signalRef, MqttExecutionEndListener::class.java) {

    override fun notify(execution: DelegateExecution?) {
        TODO("Not yet implemented")
    }
}