package com.example.auth

import kotlinx.serialization.Serializable

/**
 * Predstavlja podatke za zahtev za prijavljivanje i za lekara i za pacijenta
 *
 *
 * @property email Email adresa korisnika
 * @property password lozinka korisnika
 */

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
