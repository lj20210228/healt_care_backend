package com.example.auth.service

import com.example.auth.AuthResponse
import com.example.auth.LoginRequest
import com.example.auth.RegisterRequest
import com.example.domain.User

/**
 * Interfejs koji definiše metode za autentikaciju i rad sa korisnicima.
 */

interface AuthService {


    /**
     * Registruje novog korisnika
     *
     * @param params [RegisterRequest] objekat koji sadrzi podatke za registraciju korisnika
     * @return [AuthResponse] vraca podatke o registraciji
     */
    suspend fun registerUser(params: RegisterRequest): AuthResponse?

    /**
     * Metoda za prijavljivanje(login) korisnika
     *
     * @param params [LoginRequest] objekat koji sadrzi podatke za prijavu korisnika
     * @return [AuthResponse] vraca podatke o odgovoru sa servera
     */
    suspend fun loginUser(params: LoginRequest): AuthResponse?
    /**
     * Pretražuje korisnika po email adresi.
     *
     * @param email Email adresa korisnika koju tražimo.
     * @return [User]  Vraca korisnika sa datom email adresom, ili null ako korisnik ne postoji.
     */
    suspend fun findUserByEmail(email: String): User?
}