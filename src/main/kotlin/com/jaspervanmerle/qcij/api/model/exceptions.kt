package com.jaspervanmerle.qcij.api.model

class APIException(message: String) : Exception(message)
class InvalidCredentialsException : Exception("Invalid credentials")
