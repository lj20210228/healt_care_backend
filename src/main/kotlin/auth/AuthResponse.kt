package com.example.auth

import kotlinx.serialization.Serializable

/**
 * Odgovor servera na zahtev za autentikaciju
 *
 * @property userId Id iz UserTable
 * @property email Email korisnika
 * @property role Uloga(lekar ili pacijent)
 * @property token Auth token
 */

@Serializable
data class AuthResponse(
    val userId: String,
    val email: String,
    val role: Role,
    val token: String?=null
)
