package com.example.auth

import kotlinx.serialization.Serializable

/**
 * Uloge koje korisnik moze imati u sistemu
 */

@Serializable
enum class Role {
    ROLE_PATIENT,ROLE_DOCTOR
}