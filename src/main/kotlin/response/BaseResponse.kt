package com.example.response

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable


/**
 * Model za sve API odgovore
 *
 * @param T tip podataka koji se vraca u odgovoru
 * @property statusCode HTTP status kod koji opisuje rezultat operacije
 */
@Serializable
sealed class BaseResponse< T>(
    open val statusCode: Int= HttpStatusCode.OK.value
) {
    /**
     * Predstavlja uspešan odgovor API-ja.
     *
     * @param T Tip podataka koji se vraća u odgovoru.
     * @property data Podaci vraćeni od strane API-ja (mogu biti `null`).
     * @property message Opciona poruka (npr. "Uspešno kreiran korisnik").
     */

    @Serializable
    data class SuccessResponse<T>(
        val data:@Serializable T? = null,
        val message: String? = null,
    ): BaseResponse<T>(HttpStatusCode.OK.value)

    /**
     * Predstavlja neuspešan odgovor API-ja.
     *
     * @param T Tip podataka (obično se ne koristi u error slučaju, ali omogućava generički tip).
     * @property exception Naziv ili opis izuzetka (npr. `IllegalArgumentException`).
     * @property message Poruka o grešci (npr. "Email već postoji").
     */

    @Serializable
    data class ErrorResponse<T>(
        val exception: String?= null,
        val message: String? = null,

        ) : BaseResponse<T>(HttpStatusCode.BadRequest.value)

}