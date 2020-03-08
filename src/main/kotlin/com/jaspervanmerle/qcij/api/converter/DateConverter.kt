package com.jaspervanmerle.qcij.api.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import java.text.SimpleDateFormat
import java.util.Date

class DateConverter : Converter {
    private val sdf = SimpleDateFormat("yyyy-LL-dd HH:mm:ss")

    override fun canConvert(cls: Class<*>) = cls == Date::class.java

    override fun toJson(value: Any): String = sdf.format(value)
    override fun fromJson(jv: JsonValue) = sdf.parse(jv.string)
}
