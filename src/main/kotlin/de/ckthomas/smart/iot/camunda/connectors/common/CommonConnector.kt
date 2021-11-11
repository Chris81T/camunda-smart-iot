package de.ckthomas.smart.iot.camunda.connectors.common

import com.google.gson.Gson
import de.ckthomas.smart.iot.Constants
import de.ckthomas.smart.iot.exceptions.SmartIotException
import de.ckthomas.smart.iot.logFor
import de.ckthomas.smart.iot.services.RestServiceClient
import de.ckthomas.smart.iot.services.RestServiceClientFactory
import okhttp3.Response
import org.camunda.connect.impl.AbstractConnector
import org.camunda.connect.impl.AbstractConnectorRequest
import org.camunda.connect.impl.AbstractConnectorResponse
import org.camunda.connect.spi.Connector
import org.camunda.connect.spi.ConnectorResponse
import java.io.IOException

/**
 * Author: Christian Thomas
 * Created on: 05.10.2021
 *
 * Check license details @ project root
 */
class CommonResponse(private val response: Response? = null) : AbstractConnectorResponse() {

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
    private val gson = Gson()

    private fun checkParam(fallbackValue: String, key: String, requestParameters: Map<String, Any>): String =
        if (requestParameters.containsKey(key)) requestParameters[key] as String else fallbackValue

    private fun getRestServiceClient(requestParameters: Map<String, Any>): RestServiceClient? {
        return if (RestServiceClientFactory.isNotInstantiated()) {
            val basePath: String = checkParam(basePath, Constants.Common.BASE_PATH, requestParameters)
            val authKey: String = checkParam(authKey, Constants.Common.AUTH_KEY, requestParameters)
            val authValue: String = checkParam(authValue, Constants.Common.AUTH_VAL, requestParameters)
            RestServiceClientFactory.getInstance(basePath, authKey, authValue)
        } else {
            RestServiceClientFactory.getInstance()
        }
    }

    protected fun toJson(map: Map<String, Any>): String = gson.toJson(map)

    protected fun createUrl(path: String, domain: String, service: String): String = "$path/$domain/$service"

    protected fun createServiceUrl(domain: String, service: String): String =
        createUrl(Constants.Common.PATH_SERVICES, domain, service)

    override fun createRequest(): CommonRequest = CommonRequest(this)

    protected fun perform(request: CommonRequest, url: String?, jsonBody: String?): ConnectorResponse? {
        return try {
            val requestParameters = request.requestParameters
            LOG.info(
                "Executing operation. Given common-request = {}, given request parameters = {}, given url = {}, " +
                        "given json-body = {}",
                request,
                requestParameters,
                url,
                jsonBody
            )

            val serviceClient = getRestServiceClient(requestParameters)
            val serviceResponse = serviceClient!!.execute(url, jsonBody)

            if (serviceResponse.isSuccessful) {
                val response = CommonResponse(serviceResponse)
                LOG.info("Service call is executed. Response = {}", response)
                response
            } else {
                val failure = serviceResponse.body!!.string()
                LOG.error("Http Rest Request Execution failed! Message = {}", failure)
                throw SmartIotException(failure)
            }

        } catch (e: IOException) {
            LOG.error("Something went wrong during service execution!", e)
            throw SmartIotException(
                "Could not perform execution. IOException (Service Call). Error Message = " +
                        e.message, e
            )
        } catch (e: Exception) {
            LOG.error("Something went wrong during execution!", e)
            throw SmartIotException("Could not perform execution. Error Message = " + e.message, e)
        }
    }

    override fun execute(request: CommonRequest): ConnectorResponse {
        val requestParams = request.requestParameters

        LOG.info("About to execute CommonConnector with given request parameters = {}", requestParams)

        val jsonBody = requestParams[Constants.Common.KEY_JSON_BODY] as String
        val path = requestParams[Constants.Common.KEY_URL_PATH] as String
        val domain = requestParams[Constants.Common.KEY_URL_DOMAIN] as String
        val service = requestParams[Constants.Common.KEY_URL_SERVICE] as String

        return null!! // perform(request, createUrl(path, domain, service), jsonBody)
    }

}