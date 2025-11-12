package com.example.upaymimoni.domain.model

sealed class UpdateUserError {
    object InvalidEmail : UpdateUserError()
    object InvalidName : UpdateUserError()
    object InvalidNumber : UpdateUserError()
    object EmailInUse : UpdateUserError()
    object PermissionDenied : UpdateUserError()
    object RequiresNewLogin : UpdateUserError()
    object NetworkUnavailable : UpdateUserError()
    object NotFound : UpdateUserError()
    data class Unknown(val message: String?) : UpdateUserError()
}