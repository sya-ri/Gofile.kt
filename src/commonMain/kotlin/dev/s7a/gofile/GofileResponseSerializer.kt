package dev.s7a.gofile

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Serializer for [GofileResponse].
 */
internal class GofileResponseSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<GofileResponse<T>> {
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor = buildSerialDescriptor("GofileResponse", PolymorphicKind.SEALED) {
        element(
            "Ok",
            buildClassSerialDescriptor("Ok") {
                element<String>("status")
                element("data", dataSerializer.descriptor)
            },
        )
        element(
            "Error",
            buildClassSerialDescriptor("Error") {
                element<String>("status")
                element<JsonObject>("data", isOptional = true)
            },
        )
    }

    override fun deserialize(decoder: Decoder): GofileResponse<T> {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement() as? JsonObject ?: throw IllegalArgumentException("decoder.decodeJsonElement() must be JsonObject")
        val statusElement = element["status"] ?: throw IllegalArgumentException("element must contain status")
        val dataElement = element["data"]
        return when (val status = statusElement.jsonPrimitive.content) {
            "ok" -> {
                if (dataElement == null) throw IllegalArgumentException("element must contain data")
                GofileResponse.Ok(decoder.json.decodeFromJsonElement(dataSerializer, dataElement))
            }
            else -> {
                GofileResponse.Error(status, dataElement?.jsonObject)
            }
        }
    }

    override fun serialize(encoder: Encoder, value: GofileResponse<T>) {
        require(encoder is JsonEncoder)
        val element = buildJsonObject {
            when (value) {
                is GofileResponse.Ok -> {
                    put("status", "ok")
                    put("data", encoder.json.encodeToJsonElement(dataSerializer, value.data))
                }
                is GofileResponse.Error -> {
                    put("status", value.status)
                    if (value.data != null) {
                        put("data", value.data)
                    }
                }
            }
        }
        encoder.encodeJsonElement(element)
    }
}
