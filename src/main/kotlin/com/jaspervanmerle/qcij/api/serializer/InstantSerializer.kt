package com.jaspervanmerle.qcij.api.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.Instant
import java.time.format.DateTimeFormatter

class InstantSerializer(private val formatter: DateTimeFormatter) : StdSerializer<Instant>(Instant::class.java) {
    override fun serialize(value: Instant?, gen: JsonGenerator, provider: SerializerProvider) {
        if (value == null) {
            gen.writeNull()
            return
        }

        gen.writeString(formatter.format(value))
    }
}
