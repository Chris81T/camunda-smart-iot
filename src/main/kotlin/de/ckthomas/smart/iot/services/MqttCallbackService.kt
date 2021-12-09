package de.ckthomas.smart.iot.services

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import de.ckthomas.smart.iot.logFor
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
    val topic: String,
    val callbackFn: (topic: String, message: MqttMessage) -> Unit,
    val connectionLostFn: (cause: Throwable) -> Unit = {}
)

enum class ValueTypes {
    PRIMITIVE,
    JSON,
    ARRAY,
    UNKNOWN
}

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

    private val logger = logFor(MqttCallbackService::class.java)

    private val subscriptions = HashMap<String, MqttSubscription>()
    private val gson = Gson()

    override fun connectionLost(cause: Throwable) {
        logger.warn("Some connection lost! Throwable message = {}", cause.message)
        subscriptions.values.forEach {
            it.connectionLostFn(cause)
        }
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        logger.info("Incoming message = {} for topic = {}", message, topic)
        subscriptions.values
            .filter { it.topic == topic }
            .forEach { it.callbackFn(topic, message) }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken) {
        logger.info("Delivery is complete. Token message = {}", token.message)
    }

    /**
     * Use this method to subscribe your desired topic. When a message for that topic will arrive, the appropriate
     * callbackFn will be called.
     */
    fun subscribe(subscription: MqttSubscription): String {
        val id = UUID.randomUUID().toString()
        logger.info("About to add incoming subscription = {} to internal map with id = {}.", subscription, id)
        subscriptions[id] = subscription
        return id
    }

    fun unsubscribe(subscriptionId: String) {
        logger.info("About to remove subscription with id = {}", subscriptionId)
        subscriptions.remove(subscriptionId)
    }

    fun disconnect() {
        mqttClient.disconnect()
    }

    fun checkMessageType(message: MqttMessage): ValueTypes {
        val messageAsStr = message.toString()
        logger.info("check the type of the message (as string) = {}", messageAsStr)
        try {
            val deserializedVal = gson.fromJson(messageAsStr, Object::class.java)

            if (LinkedTreeMap::class.java == deserializedVal.`class`) {
                return ValueTypes.JSON
            }

            if (ArrayList::class.java == deserializedVal.`class`) {
                return ValueTypes.ARRAY
            }

            // all the special values are check, so it should be a primitive value
            return ValueTypes.PRIMITIVE
        } catch (e: Exception) {
            logger.error("Could not identify the required value type!", e)
            return ValueTypes.UNKNOWN
        }
    }

}