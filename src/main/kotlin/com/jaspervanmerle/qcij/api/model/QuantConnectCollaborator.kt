package com.jaspervanmerle.qcij.api.model

import com.google.gson.annotations.SerializedName

data class QuantConnectCollaborator(
    val id: Int,
    @SerializedName("uid") val userId: Int,
    @SerializedName("blivecontrol") val bLiveControl: Boolean,
    @SerializedName("epermission") val ePermission: String,
    @SerializedName("profileimage") val profileImageUrl: String,
    val name: String
)
