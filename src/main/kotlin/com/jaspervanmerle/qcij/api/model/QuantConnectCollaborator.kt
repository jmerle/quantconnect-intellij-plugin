package com.jaspervanmerle.qcij.api.model

import com.beust.klaxon.Json

data class QuantConnectCollaborator(
    val id: Int,
    @Json(name = "uid") val userId: Int,
    @Json(name = "blivecontrol") val bLiveControl: Boolean,
    @Json(name = "epermission") val ePermission: String,
    @Json(name = "profileimage") val profileImageUrl: String,
    val name: String
)
