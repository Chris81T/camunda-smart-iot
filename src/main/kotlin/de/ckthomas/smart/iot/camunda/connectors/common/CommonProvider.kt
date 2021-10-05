package de.ckthomas.smart.iot.camunda.connectors.common

import de.ckthomas.smart.iot.Constants
import de.ckthomas.smart.iot.logFor
import org.camunda.connect.spi.Connector
import org.camunda.connect.spi.ConnectorProvider

/**
 * Author: Christian Thomas
 * Created on: 30.09.2021
 *
 * Check license details @ project root
 */
class CommonProvider(providerClass: Class<out CommonProvider>) : ConnectorProvider {

    protected val LOG = logFor(providerClass)

    protected val authKey: String? = System.getProperty(Constants.Common.AUTH_KEY)
    protected val authVal: String? = System.getProperty(Constants.Common.AUTH_VAL)
    protected val basePath: String? = System.getProperty(Constants.Common.BASE_PATH)

    override fun getConnectorId(): String = Constants.ConnectorIds.ID_COMMON

    override fun createConnectorInstance(): Connector<*> {
        LOG.info("About to create a common connector instance for basePath = {}", basePath)
        return CommonConnector(connectorId, basePath, authKey, authVal)
    }
}