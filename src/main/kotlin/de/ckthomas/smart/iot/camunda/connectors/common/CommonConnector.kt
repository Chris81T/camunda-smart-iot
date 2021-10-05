package de.ckthomas.smart.iot.camunda.connectors.common

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

    override fun createRequest(): CommonRequest {
        TODO("Not yet implemented")
    }

    override fun execute(p0: CommonRequest?): ConnectorResponse {
        TODO("Not yet implemented")
    }

}