package com.example.domain

import kotlinx.serialization.Serializable

/**
 * Podaci o bolnici
 *
 * @property id Id bolnice
 * @property name ime bolnice
 * @property address Adresa bolnice
 * @property city Grad u kome se nalazi bolnica
 */


@Serializable
data class Hospital(
    val id: String,
    val name: String,
    val address: String,
    val city: String
)
