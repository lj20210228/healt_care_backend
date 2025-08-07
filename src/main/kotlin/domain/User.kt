package com.example.domain

import com.example.auth.Role
import kotlinx.serialization.Serializable


/**
 * Predstavlja osnovnog korisnika sistema
 *
 *
 * @property email Email adresa korisnika
 * @property password Lozinka korisnika
 * @property role je Uloga korisnika(pacijent,doktor)
 */

@Serializable
data class User(
    val email: String,
    val password: String?,
    val role: Role
)

