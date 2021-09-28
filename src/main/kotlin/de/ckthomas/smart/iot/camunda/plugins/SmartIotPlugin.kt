package de.ckthomas.smart.iot.camunda.plugins

import de.ckthomas.smart.iot.logFor
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.springframework.context.annotation.Configuration

/**
 * Author: Christian Thomas
 * Created on: 28.09.2021
 *
 * Check license details @ project root
 */
@Configuration
class SmartIotPlugin : AbstractProcessEnginePlugin() {

    private val LOG = logFor(SmartIotPlugin::class.java)

    override fun preInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
        super.preInit(processEngineConfiguration)
        LOG.info("Pre-initiation phase...")
    }

    override fun postInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
        super.postInit(processEngineConfiguration)
        LOG.info("Post-initiation phase...")
    }

}