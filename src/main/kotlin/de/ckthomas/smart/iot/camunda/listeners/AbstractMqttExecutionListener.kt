package de.ckthomas.smart.iot.camunda.listeners

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.logFor
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties
import org.camunda.bpm.model.xml.instance.ModelElementInstance

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
abstract class AbstractMqttExecutionListener(
    listenerClass: Class<out AbstractMqttExecutionListener>
) : ExecutionListener {

    private var signalName: String? = null
    var signalRef: String? = null

    protected val LOG = logFor(listenerClass)

    protected fun getSignalName(execution: DelegateExecution): String =
        signalName ?: execution.processEngineServices
            .repositoryService
            .getBpmnModelInstance(execution.processDefinitionId)
            .getModelElementById<ModelElementInstance>(signalRef ?: throw NoSuchElementException("No signalRef is given!"))
            .getAttributeValue(IotConstants.EngineListener.ELEM_SIGNAL_NAME)

    protected fun getResultVariableName(execution: DelegateExecution): String? =
        execution.bpmnModelElementInstance
            .extensionElements
            .elementsQuery
            .filterByType(CamundaProperties::class.java)
            .singleResult()
            .camundaProperties.firstOrNull { camundaProperty -> IotConstants.EngineListener.EXT_PROP_RESULT_VAR_NAME == camundaProperty.camundaName }
             ?.camundaValue

}