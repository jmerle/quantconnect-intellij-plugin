package com.jaspervanmerle.qcij.api

class APIException(message: String) : Exception(message)
class InvalidCredentialsException : Exception("Invalid credentials")
