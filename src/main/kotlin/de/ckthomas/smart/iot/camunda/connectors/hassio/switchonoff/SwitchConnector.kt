package de.ckthomas.smart.iot.camunda.connectors.hassio.switchonoff

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.camunda.connectors.common.CommonConnector
import de.ckthomas.smart.iot.camunda.connectors.common.CommonRequest
import org.camunda.connect.spi.ConnectorResponse

/**
 * Author: Christian Thomas
 * Created on: 28.11.2021
 *
 * https://www.home-assistant.io/integrations/switch
 *
 * service values: turn_on, turn_off, toggle
 *
 * Check license details @ project root
 */
class SwitchConnector(
    connectorId: String,
    private val basePath: String,
    private val authKey: String,
    private val authValue: String) : CommonConnector(connectorId, basePath, authKey, authValue) {

    override fun execute(request: CommonRequest): ConnectorResponse {
        val requestParameters = request.requestParameters
        logger.info("About to execute {} with given request parameters = {}", javaClass.simpleName, requestParameters)

        val service = requestParameters[IotConstants.Common.KEY_URL_SERVICE] as String?
        val url = createServiceUrl(IotConstants.Switch.DOMAIN, service!!)

        val jsonMap: MutableMap<String, Any?> = HashMap()
        jsonMap[IotConstants.Switch.JSON_BODY_ENTITY_ID] = requestParameters[IotConstants.Switch.JSON_BODY_ENTITY_ID]
        val jsonBody = toJson(jsonMap)

        return super.perform(request, url, jsonBody)
    }
}