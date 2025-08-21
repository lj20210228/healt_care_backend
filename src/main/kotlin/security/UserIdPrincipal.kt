package com.example.security

import io.ktor.server.auth.Principal


/**
 * Principal koji predstavlja autentifikovanog korisnika u aplikaciji
 *
 * @property id Jedinstveni identifikator korisnika (iz baze)
 *
 * Ova klasa e koristi prilikom autentikacije i prosledjuje se kroz Ktor authentication pipeline
 * kako bi se znalo koji korisnik je prijavljen
 */

data class UserIdPrincipal (
    val id: Int
): Principal