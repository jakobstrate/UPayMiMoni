package com.example.upaymimoni.data.mappers

interface ErrorMapper<E: Exception, D: Any> {
    fun map(error: E): D
}