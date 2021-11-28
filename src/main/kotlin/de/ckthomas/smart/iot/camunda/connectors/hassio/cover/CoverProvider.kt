package de.ckthomas.smart.iot.camunda.connectors.hassio.cover

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.camunda.connectors.common.CommonProvider
import org.camunda.connect.spi.Connector

/**
 * Author: Christian Thomas
 * Created on: 28.11.2021
 *
 * Check license details @ project root
 */
class CoverProvider : CommonProvider(CoverProvider::class.java) {

    override fun getConnectorId(): String {
        return IotConstants.ConnectorIds.ID_COVER
    }

    override fun createConnectorInstance(): Connector<*> {
        return createConnectorInstanceSafety(CoverConnector::class.java) {
                connectorId: String,
                basePath: String,
                authKey: String,
                authValue: String -> CoverConnector(connectorId, basePath, authKey, authValue)
        }
    }
}