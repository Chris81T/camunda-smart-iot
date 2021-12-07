package de.ckthomas.smart.iot.services

import de.ckthomas.smart.iot.logFor
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage


/**
 * Author: Christian Thomas
 * Created on: 07.12.2021
 *
 * Holds all relevant information for a subscription including a callback, that will be invoked while incoming message
 * is arrived.
 *
 * Check license details @ project root
 */
data class MqttSubscription(
    val subscriptionId: String,
    val topic: String,
    val callbackFn: (topic: String, message: MqttMessage) -> Unit,
    val connectionLostFn: (cause: Throwable) -> Unit = {}
)

/**
 * Author: Christian Thomas
 * Created on: 07.12.2021
 *
 * One common Mqtt Service for different scenarios.
 *
 * An interesting component can auto-wire this service to subscribe and unsubscribe topics. With the subscription a
 * callback is given, so when a message is arrived that callback will be invoked till it will be unsubscribed.
 *
 * subscriptions -> List<MqttSubscription>>
 *
 * Check license details @ project root
 */
class MqttCallbackService(private val mqttClient: MqttAsyncClient) : MqttCallback {

    override fun connectionLost(cause: Throwable) {
        TODO("Not yet implemented")
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        TODO("Not yet implemented")
    }

    override fun deliveryComplete(token: IMqttDeliveryToken) {
        TODO("Not yet implemented")
    }

}