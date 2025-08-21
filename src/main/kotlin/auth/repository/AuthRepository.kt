package com.example.auth.repository

import com.example.auth.AuthResponse
import com.example.auth.LoginRequest
import com.example.auth.RegisterRequest
import com.example.response.BaseResponse
/**
 * Repozitorijum za autentikaciju korisnika.
 *
 * Ovaj interfejs definiše osnovne operacije za registraciju i prijavu korisnika
 * i vraća rezultat u obliku [BaseResponse] koji može biti uspešan ili neuspešan.
 */

interface AuthRepository {
    /**
     * Registruje novog korisnika.
     *
     * @param user [RegisterRequest] objekat sa podacima za registraciju
     *             (npr. email, lozinka, rola, dodatne informacije).
     * @return [BaseResponse] koji sadrži [AuthResponse] u slučaju uspeha ili
     *         [BaseResponse.ErrorResponse] sa detaljima greške.
     */
    suspend fun registerUser(user: RegisterRequest): BaseResponse<AuthResponse>


    /**
     * Prijavljuje korisnika (login).
     *
     * @param user [LoginRequest] objekat sa kredencijalima korisnika
     *             (npr. email i lozinka).
     * @return [BaseResponse] koji sadrži [AuthResponse] u slučaju uspešnog logina
     *         ili [BaseResponse.ErrorResponse] ako login nije uspeo.
     */
    suspend fun loginUser(user: LoginRequest): BaseResponse<AuthResponse>



}