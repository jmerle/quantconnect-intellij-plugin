package com.jaspervanmerle.qcij.api.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import org.json.JSONObject

class JSONObjectConverter : Converter {
    override fun canConvert(cls: Class<*>) = cls == JSONObject::class.java

    override fun toJson(value: Any): String = (value as JSONObject).toString()
    override fun fromJson(jv: JsonValue) = if (jv.obj != null) JSONObject(jv.obj!!.toJsonString()) else null
}
