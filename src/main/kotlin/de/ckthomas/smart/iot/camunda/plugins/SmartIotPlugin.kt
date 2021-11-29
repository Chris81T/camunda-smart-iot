package de.ckthomas.smart.iot.camunda.plugins

import de.ckthomas.smart.iot.camunda.listeners.SmartIotParseListener
import de.ckthomas.smart.iot.logFor
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.springframework.context.annotation.Configuration
import java.util.ArrayList

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
@Configuration
class SmartIotPlugin(private val parseListener: SmartIotParseListener) : AbstractProcessEnginePlugin() {

    private val logger = logFor(SmartIotPlugin::class.java)

    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
        super.preInit(processEngineConfiguration)
        logger.info("Pre-initiation phase...")

        processEngineConfiguration?.let {
            val listeners = it.customPreBPMNParseListeners ?: ArrayList()
            listeners.add(parseListener)
            it.customPreBPMNParseListeners = listeners
        } ?: logger.warn("No process engine configuration is given!")
    }

    override fun postInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
        super.postInit(processEngineConfiguration)
        logger.info("Post-initiation phase...")
    }

}