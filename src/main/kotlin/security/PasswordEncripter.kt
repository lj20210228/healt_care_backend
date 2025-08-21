package com.example.security

import at.favre.lib.crypto.bcrypt.BCrypt

/**
 * Hešira lozinku pomoću bcrypt algoritma.
 *
 * @param password Lozinka u čistom tekstu.
 * @return Heširana lozinka (hash) koju čuvaš u bazi.
 */
fun hashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray())
}

/**
 * Proverava da li je uneta lozinka ista kao heširana u bazi.
 *
 * @param password Lozinka koju korisnik unosi.
 * @param hashedPassword Heširana lozinka koja se čuva u bazi.
 * @return true ako se lozinke poklapaju, false inače.
 */
fun verifyPassword(password: String, hashedPassword: String): Boolean {
    val result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword)
    return result.verified
}
