package de.ckthomas.smart.iot.camunda.connectors.hassio.light

import de.ckthomas.smart.iot.IotConstants
import de.ckthomas.smart.iot.camunda.connectors.common.CommonConnector
import de.ckthomas.smart.iot.camunda.connectors.common.CommonRequest
import org.camunda.connect.spi.ConnectorResponse

/**
 * Author: Christian Thomas
 * Created on: 28.11.2021
 *
 * https://www.home-assistant.io/integrations/light
 *
 * service values: turn_on, turn_off
 *
 * turn_on has different optional parameters to control the light. Check out the documentation
 *
 * Check license details @ project root
 */
class LightConnector(
    connectorId: String,
    private val basePath: String,
    private val authKey: String,
    private val authValue: String) : CommonConnector(connectorId, basePath, authKey, authValue) {

    /**
     * Every parameter, known from the documentation, will be tracked here. Only that keys are recognized by that
     * connector.
     */
    private val optionalParamKeys = listOf(
        "transition",
        "profile",
        "hs_color",
        "xy_color",
        "rgb_color",
        "rgbw_color",
        "rgbww_color",
        "color_temp",
        "kelvin",
        "color_name",
        "brightness",
        "brightness_pct",
        "brightness_step",
        "brightness_step_pct",
        "flash",
        "effect"
    )

    override fun execute(request: CommonRequest): ConnectorResponse {
        val requestParameters = request.requestParameters
        LOG.info("About to execute {} with given request parameters = {}", javaClass.simpleName, requestParameters)

        val service = requestParameters[IotConstants.Common.KEY_URL_SERVICE] as String?
        val url = createServiceUrl(IotConstants.Light.DOMAIN, service!!)

        val jsonMap: MutableMap<String, Any?> = HashMap()
        jsonMap[IotConstants.Light.JSON_BODY_ENTITY_ID] = requestParameters[IotConstants.Light.JSON_BODY_ENTITY_ID]

        // TODO make the better immutable way! (copy paste old java code here...)
        optionalParamKeys.stream()
            .filter { key: String? -> requestParameters.containsKey(key) }
            .forEach { optionalKey: String? -> jsonMap[optionalKey!!] = requestParameters[optionalKey] }

        val jsonBody = toJson(jsonMap)

        return super.perform(request, url, jsonBody)
    }
}