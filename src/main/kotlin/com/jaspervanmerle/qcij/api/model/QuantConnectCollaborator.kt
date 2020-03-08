package com.jaspervanmerle.qcij.api.model

// Taken from https://github.com/trestrantham/quantconnect-filesync/blob/master/src/types.ts

data class QuantConnectCollaborator(
    val id: Int,
    val uid: Int,
    val blivecontrol: Boolean,
    val epermission: String,
    val profileimage: String,
    val name: String
)
