package com.example.domain

import kotlinx.serialization.Serializable
import java.util.UUID


/**
 * Podaci o pacijentu
 *
 *
 * @property fullName  ime i prezime korisnika
 * @property id  id pacijenta u tabeli
 * @see user povezani korisnik iz tabele User
 * @property selectedDoctor  id izabranog doktora kog korisnik bira
 * @property selectedHospital  id izabranog zdravstvenog centra gde ce se nalaziti korisnikov zdravstveni kartor
 */

@Serializable
data class Patient(
    val id: String,
    val user: User,
    val fullName: String,
    val selectedDoctor: String?=null,
    val healtCareCenter: String,
)
