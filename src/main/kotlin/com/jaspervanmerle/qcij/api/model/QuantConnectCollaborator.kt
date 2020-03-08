package com.jaspervanmerle.qcij.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class QuantConnectCollaborator(
    val id: Int,
    @JsonProperty("uid") val userId: Int,
    @JsonProperty("blivecontrol") val bLiveControl: Boolean,
    @JsonProperty("epermission") val ePermission: String,
    @JsonProperty("profileimage") val profileImageUrl: String,
    val name: String
)
