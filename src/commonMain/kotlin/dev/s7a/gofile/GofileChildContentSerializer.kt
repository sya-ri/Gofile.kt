package dev.s7a.gofile

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put

/**
 * Serializer for [GofileChildContent].
 */
public class GofileChildContentSerializer : KSerializer<GofileChildContent> {
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("GofileChildContent", PolymorphicKind.SEALED) {
        element(
            "File",
            buildClassSerialDescriptor("File") {
                element<String>("id")
                element<GofileContentType>("type")
                element<String>("name")
                element<String>("parentFolder")
                element<Long>("createTime")
                element<Double>("size")
                element<Int>("downloadCount")
                element<String>("md5")
                element<String>("mimetype")
                element<String>("serverChoosen")
                element<String>("directLink")
                element<String>("link")
                element<String?>("thumbnail", isOptional = true)
            },
        )
        element(
            "Folder",
            buildClassSerialDescriptor("Folder") {
                element<String>("id")
                element<GofileContentType>("type")
                element<String>("name")
                element<String>("parentFolder")
                element<Long>("createTime")
                element<List<String>>("childs")
                element<String>("code")
                element<Boolean>("public")
                element<String>("password", isOptional = true)
                element<String?>("description", isOptional = true)
                element<Long?>("expire", isOptional = true)
                element<String>("tags", isOptional = true)
            },
        )
    }

    override fun deserialize(decoder: Decoder): GofileChildContent {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement() as? JsonObject ?: throw IllegalArgumentException("decoder.decodeJsonElement() must be JsonObject")
        val id = element["id"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain id")
        val type = element["type"]?.let { decoder.json.decodeFromJsonElement<GofileContentType>(it) } ?: throw IllegalArgumentException("element must contain type")
        val name = element["name"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain name")
        val parentFolder = element["parentFolder"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain parentFolder")
        val createTime = element["createTime"]?.jsonPrimitive?.long ?: throw IllegalArgumentException("element must contain createTime")
        return when (type) {
            GofileContentType.File -> {
                val size = element["size"]?.jsonPrimitive?.double ?: throw IllegalArgumentException("element must contain size")
                val downloadCount = element["downloadCount"]?.jsonPrimitive?.int ?: throw IllegalArgumentException("element must contain downloadCount")
                val md5 = element["md5"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain md5")
                val mimetype = element["mimetype"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain mimetype")
                val serverChoosen = element["serverChoosen"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain serverChoosen")
                val directLink = element["directLink"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain directLink")
                val link = element["link"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain link")
                val thumbnail = element["thumbnail"]?.jsonPrimitive?.content
                GofileChildContent.File(id, name, parentFolder, createTime, size, downloadCount, md5, mimetype, serverChoosen, directLink, link, thumbnail)
            }
            GofileContentType.Folder -> {
                val childs = element["childs"]?.jsonArray?.map { it.jsonPrimitive.content } ?: throw IllegalArgumentException("element must contain childs")
                val code = element["code"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("element must contain code")
                val public = element["public"]?.jsonPrimitive?.boolean ?: throw IllegalArgumentException("element must contain public")
                val password = element["password"]?.jsonPrimitive?.boolean ?: false
                val description = element["description"]?.jsonPrimitive?.content
                val expire = element["expire"]?.jsonPrimitive?.long
                val tags = element["tags"]?.jsonPrimitive?.content?.split(",").orEmpty()
                GofileChildContent.Folder(id, name, parentFolder, createTime, childs, code, public, password, description, expire, tags)
            }
        }
    }

    override fun serialize(encoder: Encoder, value: GofileChildContent) {
        require(encoder is JsonEncoder)
        val element = buildJsonObject {
            put("id", value.id)
            put("type", encoder.json.encodeToJsonElement(GofileContentType.serializer(), value.type))
            put("name", value.name)
            put("parentFolder", value.parentFolder)
            put("createTime", value.createTime)
            when (value) {
                is GofileChildContent.File -> {
                    put("size", value.size)
                    put("downloadCount", value.downloadCount)
                    put("md5", value.md5)
                    put("mimetype", value.mimetype)
                    put("serverChoosen", value.serverChoosen)
                    put("directLink", value.directLink)
                    put("link", value.link)
                    if (value.thumbnail != null) put("thumbnail", value.thumbnail)
                }
                is GofileChildContent.Folder -> {
                    put("childs", encoder.json.encodeToJsonElement(value.childs))
                    put("code", value.code)
                    put("public", value.public)
                    if (value.password) put("password", true)
                    if (value.description != null) put("description", value.description)
                    if (value.expire != null) put("expire", value.expire)
                    if (value.tags.isNotEmpty()) put("tags", value.tags.joinToString(","))
                }
            }
        }
        encoder.encodeJsonElement(element)
    }
}
