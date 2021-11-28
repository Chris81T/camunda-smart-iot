package de.ckthomas.smart.iot.camunda.connectors.hassio.light

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.camunda.connectors.common.CommonConnector
import de.ckthomas.smart.iot.camunda.connectors.common.CommonProvider
import de.ckthomas.smart.iot.multiLet
import org.camunda.connect.spi.Connector

/**
 * Author: Christian Thomas
 * Created on: 28.11.2021
 *
 * Check license details @ project root
 */
class LightProvider : CommonProvider(LightProvider::class.java) {

    override fun getConnectorId(): String {
        return IotConstants.ConnectorIds.ID_LIGHT
    }

    override fun createConnectorInstance(): Connector<*> {
        return createConnectorInstanceSafety(LightConnector::class.java) {
                connectorId: String,
                basePath: String,
                authKey: String,
                authValue: String -> LightConnector(connectorId, basePath, authKey, authValue)
        }
    }
}