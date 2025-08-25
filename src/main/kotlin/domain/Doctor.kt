package com.example.domain

import kotlinx.serialization.Serializable


/**
 *
 * Podaci o lekaru
 *
 * @property id  Id lekara,
 * @property fullName  ime i prezime lekara
 * @property userId  Referenca na njegove podatke u UserTable
 * @property specialization  ime njegove specijalizacije
 * @property healtCareCenter  id zdravstvenog centra u kome radi
 * @property maxPatients  broj koliko maksimalno ima pacijenata kojima moze biti izabrani lekar
 * @property currentPatients  trenutan broj pacijenata kojima je izabrani lekar
 * @property isGeneral odredjuje da li je lekar opste prakse
 */

@Serializable
data class Doctor(
    val id: String,
    val userId: String,
    val fullName: String,
    val specialization: String,
    val healtCareCenter: String,
    val maxPatients:Int?=null,
    val currentPatients: Int=0,
    val isGeneral: Boolean?=null
)
