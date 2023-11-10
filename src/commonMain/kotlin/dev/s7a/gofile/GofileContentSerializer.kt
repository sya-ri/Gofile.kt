package dev.s7a.gofile

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put

/**
 * Serializer for [GofileContent].
 */
public class GofileContentSerializer : KSerializer<GofileContent> {
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("GofileContent", PolymorphicKind.SEALED) {
        element(
            "File",
            PrimitiveSerialDescriptor("File", PrimitiveKind.STRING),
        )
        element(
            "Folder",
            buildClassSerialDescriptor("Folder") {
                element<Boolean>("isOwner")
                element<String>("id")
                element<GofileContentType>("type")
                element<String>("name")
                element<String>("parentFolder")
                element<String>("code")
                element<Long>("createTime")
                element<Boolean>("public")
                element<List<String>>("childs")
                element<Int>("totalDownloadCount")
                element<Double>("totalSize")
                element("contents", mapSerialDescriptor(String.serializer().descriptor, GofileChildContent.serializer().descriptor))
            },
        )
    }

    override fun deserialize(decoder: Decoder): GofileContent {
        require(decoder is JsonDecoder)
        return when (val element = decoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                if (element.content == "not-a-folder") {
                    GofileContent.File
                } else {
                    throw IllegalArgumentException("JsonPrimitive#content must be 'not-a-folder'. Actually ${element.content}")
                }
            }
            is JsonObject -> {
                val isOwner = element["isOwner"]?.jsonPrimitive?.boolean ?: throw IllegalArgumentException("element must contain isOwner")
                val id = element["id"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain id")
                val type = element["type"]?.let { decoder.json.decodeFromJsonElement(GofileContentType.serializer(), it) } ?: throw IllegalArgumentException("element must contain type")
                if (type != GofileContentType.Folder) throw IllegalArgumentException("type must be folder")
                val name = element["name"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain name")
                val parentFolder = element["parentFolder"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain parentFolder")
                val code = element["code"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain code")
                val createTime = element["createTime"]?.jsonPrimitive?.long ?: throw IllegalArgumentException("element must contain createTime")
                val public = element["public"]?.jsonPrimitive?.boolean ?: throw IllegalArgumentException("element must contain public")
                val childs = element["childs"]?.jsonArray?.map { it.jsonPrimitive.content } ?: throw IllegalArgumentException("element must contain childs")
                val totalDownloadCount = element["totalDownloadCount"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("element must contain totalDownloadCount")
                val totalSize = element["totalSize"]?.jsonPrimitive?.double ?: throw IllegalArgumentException("element must contain totalSize")
                val contents = element["contents"]?.jsonObject?.entries?.associate { it.key to decoder.json.decodeFromJsonElement(GofileChildContent.serializer(), it.value) } ?: throw IllegalArgumentException("element must contain contents")
                GofileContent.Folder(isOwner, id, name, parentFolder, code, createTime, public, childs, totalDownloadCount, totalSize, contents)
            }
            else -> {
                throw IllegalArgumentException("decoder.decodeJsonElement() must be JsonObject or JsonPrimitive")
            }
        }
    }

    override fun serialize(encoder: Encoder, value: GofileContent) {
        require(encoder is JsonEncoder)
        when (value) {
            GofileContent.File -> {
                encoder.encodeString("not-a-folder")
            }
            is GofileContent.Folder -> {
                val element = buildJsonObject {
                    put("isOwner", value.isOwner)
                    put("id", value.id)
                    put("type", encoder.json.encodeToJsonElement(GofileContentType.serializer(), GofileContentType.Folder))
                    put("name", value.name)
                    put("parentFolder", value.parentFolder)
                    put("code", value.code)
                    put("createTime", value.createTime)
                    put("isOwner", value.isOwner)
                    put("public", value.public)
                    put("childs", encoder.json.encodeToJsonElement(value.childs))
                    put("totalDownloadCount", value.totalDownloadCount)
                    put("totalSize", value.totalSize)
                    put("contents", encoder.json.encodeToJsonElement(MapSerializer(String.serializer(), GofileChildContent.serializer()), value.contents))
                }
                encoder.encodeJsonElement(element)
            }
        }
    }
}
