package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

/**
 * Konfiguracija i generisanje JWT tokena.
 *  Koristi singleton pristup putem [instance] i koristi HMAC256 algoritam sa zadatim tajnim ključem.
 * Tokeni su potpisani i proveravani sa istim algoritmom i sadrže claim "id" kao korisnički identifikator.
 *
 * JWT se koristi za autorizaciju korisnika u okviru zdravstvene aplikacije.
 *
 * @constructor Privatni konstruktor koji prihvata tajni ključ za generisanje tokena.
 */


class JwtConfig private constructor(secret: String) {

    private val algorithm= Algorithm.HMAC256(secret)

    /**
     * JWT verifikator koji proverava potpisan token.
     */
    val verifier
    : JWTVerifier= JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()


    /**
     * Kreira JWT pristupni token sa korisničkim ID-em kao claim-om.
     *
     * @param id Jedinstveni identifikator korisnika koji se smešta kao claim u token.
     * @return String koji predstavlja generisani JWT token.
     */
    fun createAccessToken(id: String): String= JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(CLAIM,id)
        .sign(algorithm)


    companion object{
        private const val ISSUER="healt_care"
        private const val AUDIENCE="healt_care"
        /**
         * Naziv claim-a koji se koristi za čuvanje korisničkog ID-a u tokenu.
         */
        const val CLAIM="id"




        /**
         * Singleton instanca klase [JwtConfig].
         * Mora se prethodno inicijalizovati pomoću [initialize] metode.
         */

        lateinit var instance: JwtConfig
            private set

        /**
         * Inicijalizuje [JwtConfig] instancu sa datim tajnim ključem.
         * Ova metoda se mora pozvati pre korišćenja [instance].
         *
         * @param secret Tajni ključ za potpisivanje tokena.
         */
        fun initialize(secret: String){
            synchronized(this) {
                if(!this::instance.isInitialized){
                    instance= JwtConfig(secret)
                }
            }
        }
    }
}