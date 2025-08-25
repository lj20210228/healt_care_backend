package com.example.doctor

import com.example.domain.Hospital
import kotlinx.serialization.Serializable

/**
 * Klasa koja služi kao obrazar za zahtev za izmenu podataka o lekaru
 *
 * @property userId Id Lekara koga treba izmeniti
 * @property maxPatients Maksimalan broj pacijenata kojima jedan lekar može biti izabrani
 * @property hospital Id bolnice u kojoj lekar radi, može se menjati
 */
@Serializable
data class EditDoctorRequest(
    val userId: String,
    val maxPatients: Int?=null,
    val hospital: String?=null
)
