package de.ckthomas.smart.iot.camunda.connectors.hassio.cover

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.camunda.connectors.common.CommonConnector
import de.ckthomas.smart.iot.camunda.connectors.common.CommonRequest
import org.camunda.connect.spi.ConnectorResponse

/**
 * Author: Christian Thomas
 * Created on: 28.11.2021
 *
 * https://www.home-assistant.io/integrations/cover
 *
 * service values: open_cover, stop_cover, close_cover
 *
 * TODO: https://www.home-assistant.io/integrations/cover#service-coverset_cover_position --> Value between 0 - 100
 *
 * Check license details @ project root
 */
class CoverConnector(
    connectorId: String,
    private val basePath: String,
    private val authKey: String,
    private val authValue: String) : CommonConnector(connectorId, basePath, authKey, authValue) {

    override fun execute(request: CommonRequest): ConnectorResponse {
        val requestParameters = request.requestParameters
        LOG.info("About to execute {} with given request parameters = {}", javaClass.simpleName, requestParameters)

        val service = requestParameters[IotConstants.Common.KEY_URL_SERVICE] as String?
        val url = createServiceUrl(IotConstants.Cover.DOMAIN, service!!)

        val jsonMap: MutableMap<String, Any?> = HashMap()
        jsonMap[IotConstants.Cover.JSON_BODY_ENTITY_ID] = requestParameters[IotConstants.Cover.JSON_BODY_ENTITY_ID]
        val jsonBody = toJson(jsonMap)

        return super.perform(request, url, jsonBody)
    }
}