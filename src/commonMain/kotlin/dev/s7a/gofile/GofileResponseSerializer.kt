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
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Serializer for [GofileResponse]
 */
class GofileResponseSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<GofileResponse<T>> {
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor = buildSerialDescriptor("GofileResponse", PolymorphicKind.SEALED) {
        element(
            "Ok",
            buildClassSerialDescriptor("Ok") {
                element("data", dataSerializer.descriptor)
            }
        )
        element(
            "Error",
            buildClassSerialDescriptor("Error") {
                element<String>("status")
            }
        )
    }

    override fun deserialize(decoder: Decoder): GofileResponse<T> {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement() as JsonObject
        return when (val status = element["status"]!!.jsonPrimitive.content) {
            "ok" -> GofileResponse.Ok(decoder.json.decodeFromJsonElement(dataSerializer, element["data"]!!))
            else -> GofileResponse.Error(status)
        }
    }

    override fun serialize(encoder: Encoder, value: GofileResponse<T>) {
        require(encoder is JsonEncoder)
        val element = when (value) {
            is GofileResponse.Ok -> encoder.json.encodeToJsonElement(dataSerializer, value.data)
            is GofileResponse.Error -> buildJsonObject { put("status", value.status) }
        }
        encoder.encodeJsonElement(element)
    }
}
