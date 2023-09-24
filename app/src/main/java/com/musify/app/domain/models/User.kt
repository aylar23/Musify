package com.musify.app.domain.models

data class User(
    val name: String,
    val  lastName: String,
    var premium: Boolean
)

var defaultUser: User = User("Myrat", "Myradow", true)
