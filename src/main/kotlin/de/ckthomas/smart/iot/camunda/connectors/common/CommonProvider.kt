package de.ckthomas.smart.iot.camunda.connectors.common

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.exceptions.SmartIotException
import de.ckthomas.smart.iot.logFor
import de.ckthomas.smart.iot.multiLet
import org.camunda.connect.spi.Connector
import org.camunda.connect.spi.ConnectorProvider

/**
 * Author: Christian Thomas
 * Created on: 30.09.2021
 *
 * Check license details @ project root
 */
open class CommonProvider(providerClass: Class<out CommonProvider>) : ConnectorProvider {

    protected val LOG = logFor(providerClass)

    protected val authKey: String? = System.getProperty(IotConstants.Common.AUTH_KEY)
    protected val authVal: String? = System.getProperty(IotConstants.Common.AUTH_VAL)
    protected val basePath: String? = System.getProperty(IotConstants.Common.BASE_PATH)

    protected fun createConnectorInstanceSafety(connectorClass: Class<out CommonConnector>,
                                                instantiateFn: (connectorId: String, basePath: String, authKey: String,
                                                authValue: String) -> Connector<*>): Connector<*> {
        LOG.info("About to create a connector instance = {} for basePath = {}", connectorClass.simpleName, basePath)
        val values = listOf(authKey, authVal, basePath)

        if (multiLet(values)) {
            return instantiateFn(connectorId, basePath!!, authKey!!, authVal!!)
        }

        val msg = "Some of the required values - authKey, authVal or basePath or not set properly!"
        LOG.error("$msg (ConnectorType = ${connectorClass.simpleName}) Values = {}", values)

        throw SmartIotException(msg)
    }

    override fun getConnectorId(): String = IotConstants.ConnectorIds.ID_COMMON

    override fun createConnectorInstance(): Connector<*> {
        return createConnectorInstanceSafety(CommonConnector::class.java) {
                connectorId: String,
                basePath: String,
                authKey: String,
                authValue: String -> CommonConnector(connectorId, basePath, authKey, authValue)
        }
    }
}