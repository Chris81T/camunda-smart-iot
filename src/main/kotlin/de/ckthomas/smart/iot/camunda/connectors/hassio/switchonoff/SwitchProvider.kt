package de.ckthomas.smart.iot.camunda.connectors.hassio.switchonoff

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
class SwitchProvider : CommonProvider(SwitchProvider::class.java) {

    override fun getConnectorId(): String {
        return IotConstants.ConnectorIds.ID_SWITCH
    }

    override fun createConnectorInstance(): Connector<*> {
        return createConnectorInstanceSafety(SwitchConnector::class.java) {
                connectorId,
                basePath,
                authKey,
                authValue -> SwitchConnector(connectorId, basePath, authKey, authValue)
        }
    }
}