package dev.s7a.gofile

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for [GofileTier].
 */
public class GofileTierSerializer : KSerializer<GofileTier> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GofileTier", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): GofileTier {
        return GofileTier.from(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: GofileTier) {
        encoder.encodeString(value.toString())
    }
}
