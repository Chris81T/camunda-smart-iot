package de.ckthomas.smart.iot.camunda.connectors.common

import de.ckthomas.smart.iot.Constants
import de.ckthomas.smart.iot.logFor
import org.camunda.connect.impl.AbstractConnector
import org.camunda.connect.impl.AbstractConnectorRequest
import org.camunda.connect.impl.AbstractConnectorResponse
import org.camunda.connect.spi.Connector
import org.camunda.connect.spi.ConnectorResponse

/**
 * Author: Christian Thomas
 * Created on: 05.10.2021
 *
 * Check license details @ project root
 */
// TODO check, if a payload should be given as property of this class!
class CommonResponse() : AbstractConnectorResponse() {

    override fun collectResponseParameters(p0: MutableMap<String, Any>?) {
    }
}

/**
 * Author: Christian Thomas
 * Created on: 05.10.2021
 *
 * Check license details @ project root
 */
class CommonRequest(connector: Connector<*>) : AbstractConnectorRequest<CommonResponse>(connector) {}

/**
 * Author: Christian Thomas
 * Created on: 30.09.2021
 *
 * Check license details @ project root
 */
class CommonConnector(
    connectorId: String,
    private val basePath: String,
    private val authKey: String,
    private val authValue: String) : AbstractConnector<CommonRequest, CommonResponse>(connectorId) {

    private val LOG = logFor(CommonConnector::class.java)

    private fun getRestServiceClient(requestParameters: Map<String, Any>): RestServiceClient? {
        return if (RestServiceClientFactory.isNotInstantiated()) {
            val basePath: String = checkParam(basePath, PluginConsts.Common.BASE_PATH, requestParameters)
            val authKey: String = checkParam(authKey, PluginConsts.Common.AUTH_KEY, requestParameters)
            val authValue: String = checkParam(authValue, PluginConsts.Common.AUTH_VAL, requestParameters)
            RestServiceClientFactory.getInstance(basePath, authKey, authValue)
        } else {
            RestServiceClientFactory.getInstance()
        }
    }

    protected fun createUrl(path: String, domain: String, service: String) = "$path/$domain/$service"

    override fun createRequest(): CommonRequest = CommonRequest(this)

    override fun execute(request: CommonRequest): ConnectorResponse {
        val requestParams = request.requestParameters

        LOG.info("About to execute CommonConnector with given request parameters = {}", requestParams)

        val jsonBody = requestParams[Constants.Common.KEY_JSON_BODY] as String
        val path = requestParams[Constants.Common.KEY_URL_PATH] as String
        val domain = requestParams[Constants.Common.KEY_URL_DOMAIN] as String
        val service = requestParams[Constants.Common.KEY_URL_SERVICE] as String

        return perform(request, createUrl(path, domain, service), jsonBody)
    }

}