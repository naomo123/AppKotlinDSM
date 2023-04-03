package com.example.appkotlindsm.modelo

class Error {
    lateinit var key: String
    var errors: MutableList<String> = mutableListOf<String>()

    constructor(key: String, errors: MutableList<String>) {
        this.key = key
        this.errors = errors
    }
}