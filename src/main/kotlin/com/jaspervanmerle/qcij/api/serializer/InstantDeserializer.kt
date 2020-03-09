package com.jaspervanmerle.qcij.api.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.Instant
import java.time.format.DateTimeFormatter

class InstantDeserializer(private val formatter: DateTimeFormatter) : StdDeserializer<Instant>(String::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
        return formatter.parse(p.valueAsString, Instant::from)
    }
}
