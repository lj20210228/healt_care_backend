package com.example.auth

import kotlinx.serialization.Serializable


/**
 *
 * Parametri za registraciju korisnika, i za pacijente i za lekare, nisu svi atributi obavezni
 *
 *
 * @property email Email
 * @property password Sifra
 * @property fullName ime i prezime
 * @property role Lekar ili pacijent
 * @property specialization Za lekara, specijalizacija
 * @property hospital Id bolnice, za obe pozicije
 * @property maxPatients maksimalan broj pacijenata za izabranog lekara
 * @property isGeneral da li je lekar opste prakse
 * @property selectedDoctor lekar kog korisnik bira kao opsteg
 */



@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val role: Role,
    val specialization: String?=null,
    val hospital: String?=null,
    val maxPatients:Int?=null,
    val isGeneral: Boolean?=null,
    val selectedDoctor: String?=null,
)
