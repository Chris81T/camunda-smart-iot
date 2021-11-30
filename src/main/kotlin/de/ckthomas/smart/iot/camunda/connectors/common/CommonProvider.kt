package de.ckthomas.smart.iot.camunda.connectors.common

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.SpringConfig
import de.ckthomas.smart.iot.components.SpringAppContext
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
open class CommonProvider(providerClass: Class<out CommonProvider> = CommonProvider::class.java) : ConnectorProvider {

    protected val logger = logFor(providerClass)

    // Spring DI is not working here: https://forum.camunda.org/t/custom-connector-using-springs-dependency-injection/20835
    private val config: SpringConfig.HassioConfigData = SpringAppContext.getBean(SpringConfig.HassioConfigData::class.java)

    protected fun createConnectorInstanceSafety(connectorClass: Class<out CommonConnector>,
                                                instantiateFn: (connectorId: String, basePath: String, authKey: String,
                                                authValue: String) -> Connector<*>): Connector<*> {
        logger.info("About to create a connector instance = {} for basePath = {}", connectorClass.simpleName, config.basePath)

        val values = listOf(config.authKey, config.authValue, config.basePath)
        if (multiLet(values) && values.none { value -> value == IotConstants.Configuration.NOT_YET_SET }) {
            return instantiateFn(connectorId, config.basePath, config.authKey, config.authValue)
        }

        val msg = "Some of the required values - authKey, authVal or basePath or not set properly!"
        logger.error("$msg (ConnectorType = ${connectorClass.simpleName}) Values = {}", values)

        throw SmartIotException(msg)
    }

    override fun getConnectorId(): String = IotConstants.ConnectorIds.ID_COMMON

    override fun createConnectorInstance(): Connector<*> =
        createConnectorInstanceSafety(CommonConnector::class.java) {
                connectorId: String,
                basePath: String,
                authKey: String,
                authValue: String -> CommonConnector(connectorId, basePath, authKey, authValue)
        }
}