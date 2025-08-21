package com.example.auth.repository

import com.example.auth.AuthResponse
import com.example.auth.LoginRequest
import com.example.auth.RegisterRequest
import com.example.auth.service.AuthService
import com.example.response.BaseResponse

/**
 * Implementacija AuthRepository interfejsa
 *
 * @param authService Servis za komunikaciju sa bazom koji sluzi za prijavu i registraciju korisnika
 *
 */
class AuthRepositoryImplementation(val authService: AuthService): AuthRepository {

    /**
     * Funckija za registraciju
     */
    override suspend fun registerUser(user: RegisterRequest): BaseResponse<AuthResponse> {
        println("User $user")

        val isUserExist=authService.findUserByEmail(user.email)
        println("isUserExist $isUserExist")
       return if (isUserExist==null){
           val userFromServer=authService.registerUser(user)
           if(userFromServer!=null){
               BaseResponse.SuccessResponse(data = userFromServer, message = "Uspešna registracija")
           }else BaseResponse.ErrorResponse("Neuspešna registracija")
        }else
        {
            BaseResponse.ErrorResponse("Korisnik sa datim email-om već postoji")
        }
    }

    /**
     * Funckija za ulogovanje
     */
    override suspend fun loginUser(user: LoginRequest): BaseResponse<AuthResponse> {
        val userFromServer=authService.loginUser(params = user)
        return if(userFromServer!=null){
            BaseResponse.SuccessResponse(data = userFromServer, message = "Korisnik ulogovan")
        }else{
            BaseResponse.ErrorResponse("Neuspešno ulogovanje")
        }
    }
}