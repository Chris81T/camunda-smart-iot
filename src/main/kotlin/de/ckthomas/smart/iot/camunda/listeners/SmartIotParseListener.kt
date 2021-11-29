package de.ckthomas.smart.iot.camunda.listeners

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.logFor
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl
import org.camunda.bpm.engine.impl.util.xml.Element
import org.springframework.stereotype.Component

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
@Component
class SmartIotParseListener(
    private val startListener: MqttExecutionStartListener,
    private val endListener: MqttExecutionEndListener
) : AbstractBpmnParseListener() {

    private val logger = logFor(SmartIotParseListener::class.java)

    private fun decorateSignalEvent(eventDefinition: Element, activity: ActivityImpl) {
        logger.debug("Decorate given signal-event-definition: tagName = {}, text = {} with mqtt start + end listener",
            eventDefinition.tagName, eventDefinition.text)
        val signalRef = eventDefinition.attribute(IotConstants.EngineListener.ELEM_SIGNAL_REF)
        startListener.signalRef = signalRef
        endListener.signalRef = signalRef
        activity.addListener(ExecutionListener.EVENTNAME_START, startListener)
        activity.addListener(ExecutionListener.EVENTNAME_END, endListener)
    }

    override fun parseBoundarySignalEventDefinition(
        signalEventDefinition: Element?,
        interrupting: Boolean,
        signalActivity: ActivityImpl?
    ) {
        super.parseBoundarySignalEventDefinition(signalEventDefinition, interrupting, signalActivity)
        decorateSignalEvent(signalEventDefinition!!, signalActivity!!)
    }

    override fun parseIntermediateSignalCatchEventDefinition(
        signalEventDefinition: Element?,
        signalActivity: ActivityImpl?
    ) {
        super.parseIntermediateSignalCatchEventDefinition(signalEventDefinition, signalActivity)
        decorateSignalEvent(signalEventDefinition!!, signalActivity!!)
    }

}