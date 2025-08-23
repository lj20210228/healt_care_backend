package com.example.auth

import com.example.auth.repository.AuthRepository
import com.example.auth.repository.AuthRepositoryImplementation
import com.example.auth.service.AuthService
import com.example.domain.User
import com.example.response.BaseResponse
import com.fasterxml.jackson.databind.ser.Serializers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test klasa za [AuthRepositoryImplementation].
 *
 * Testira sledeće funkcionalnosti:
 * - Uspešna registracija pacijenta
 * - Uspešna registracija lekara
 * - Neuspešna registracija kada email već postoji
 * - Neuspešna registracija kada server vrati null
 * - Uspešno logovanje korisnika
 * - Neuspešno logovanje korisnika
 *
 * Koristi [io.mockk.MockK] za mocking zavisnosti i [kotlinx.coroutines.test.runTest]
 * za testiranje korutina.
 *
 *
 * -GIVEN- početni uslovi testa
 * -WHEN - akcija koja se testira
 * -THEN - očekivani ishod
 */

class AuthRepositoryImplementationTest {

    private lateinit var authService: AuthService
    private lateinit var authRepository: AuthRepository


    /**
     * Izvršava se pre svakog testa u klasi
     * Kreira mock za AuthService koristeći MockK
     * Inticijalizuje AuthRepostoryImplementation sa mockovanim AuthService
     */

    @BeforeEach
    fun setUp(){
        authService= mockk()
        authRepository= AuthRepositoryImplementation(authService)
    }
    /**
     * Testira scenario uspešne registracije pacijenta.
     *
     * GIVEN:
     * - Novi korisnik sa ulogom ROLE_PATIENT
     * - Email ne postoji u bazi
     * - AuthService vraća validan [AuthResponse]
     *
     * WHEN:
     * - Poziva se [AuthRepositoryImplementation.registerUser]
     *
     * THEN:
     * - Rezultat je [BaseResponse.SuccessResponse]
     * - Poruka je "Uspešna registracija"
     * - Podaci u odgovoru su isti kao mockovani [AuthResponse]
     */
    @Test
    fun registerUser_Uspesna_registracija_pacijenta()= runTest{
        val request= RegisterRequest(
            "test@test.com", "password123",
            fullName ="Test test" ,
            role = Role.ROLE_PATIENT,
            specialization =null,
            hospital = null,
            maxPatients = null,
            isGeneral = null,
            selectedDoctor = null
        )
        val response = AuthResponse(
            token = "dummy-token-123",
            email = "test@test.com",
            role = Role.ROLE_PATIENT,
            userId = "uuid-1"
        )
        coEvery { authService.findUserByEmail("test@test.com") }returns null
        coEvery { authService.registerUser(request) }returns response
        val result=authRepository.registerUser(request)
        assertTrue(result is BaseResponse.SuccessResponse)
        assertEquals("Uspešna registracija",(result as BaseResponse.SuccessResponse).message)
        assertEquals(response,result.data)
        coVerify(exactly = 1) { authService.findUserByEmail("test@test.com") }
        coVerify(exactly = 1) { authService.registerUser(request)}
        confirmVerified(authService)

    }

    /**
     * Testira scenario uspešne registracije lekara.
     *
     * GIVEN:
     * - Novi korisnik sa ulogom ROLE_DOCTOR
     * - Email ne postoji u bazi
     * - AuthService vraća validan [AuthResponse]
     *
     * WHEN:
     * - Poziva se [AuthRepositoryImplementation.registerUser]
     *
     * THEN:
     * - Rezultat je [BaseResponse.SuccessResponse]
     * - Poruka je "Uspešna registracija"
     * - Podaci u odgovoru su isti kao mockovani [AuthResponse]
     */

    @Test
    fun registerUser_uspesnaRegistracijaLekara()=runTest{
        val request= RegisterRequest(
            "test@test.com", "password123",
            fullName ="Test test" ,
            role = Role.ROLE_DOCTOR,
            specialization ="Kardiolog",
            hospital = null,
            maxPatients = 20,
            isGeneral = false,
            selectedDoctor = null
        )
        val response = AuthResponse(
            token = "dummy-token-123",
            email = "test@test.com",
            role = Role.ROLE_PATIENT,
            userId = "uuid-2"
        )
        coEvery { authService.findUserByEmail("test@test.com") }returns null
        coEvery { authService.registerUser(request) }returns response
        val result=authRepository.registerUser(request)
        assertTrue(result is BaseResponse.SuccessResponse)
        assertEquals("Uspešna registracija",(result as BaseResponse.SuccessResponse).message)
        assertEquals(response,result.data)
        coVerify(exactly = 1) { authService.findUserByEmail("test@test.com") }
        coVerify(exactly = 1) { authService.registerUser(request)}
        confirmVerified(authService)
    }
    /**
     * Testira scenario neuspešne registracije, jer email već postoji.
     *
     * GIVEN:
     * - Novi korisnik sa ulogom ROLE_PATIENT, može i ROLE_DOCTOR, svejedno je
     * - Email postoji u bazi
     * - AuthService vraća usera [User]
     *
     * WHEN:
     * - Poziva se [AuthRepositoryImplementation.registerUser]
     *
     * THEN:
     * - Rezultat je [BaseResponse.ErrorResponse]
     * - Poruka je "Korisnik sa datim email-om već postoji"
     */
    @Test
    fun registerUser_emailVecPostoji()=runTest{
        val request= RegisterRequest(
            "test@test.com", "password123",
            fullName ="Test test" ,
            role = Role.ROLE_DOCTOR,
            specialization ="Kardiolog",
            hospital = null,
            maxPatients = 20,
            isGeneral = false,
            selectedDoctor = null
        )
        val response = User(
            email = "test@test.com",
            role = Role.ROLE_PATIENT,
            password = "password123"
        )
        coEvery { authService.findUserByEmail("test@test.com") }returns response
        val result=authRepository.registerUser(request)
        assertTrue ( result is BaseResponse.ErrorResponse )

        assertEquals(
            "Korisnik sa datim email-om već postoji",
            (result as BaseResponse.ErrorResponse).message
        )
        coVerify(exactly = 1) {authService.findUserByEmail("test@test.com")  }
        coVerify(exactly = 0) {authService.registerUser(any())  }
        confirmVerified(authService)
    }
    /**
     * Testira scenario neuspešne registracije, jer server vrati null objekat.
     *
     * GIVEN:
     * - Novi korisnik sa ulogom ROLE_PATIENT, može i ROLE_DOCTOR, svejedno je
     * - Email ne postoji u bazi
     * - AuthService vraća null
     *
     * WHEN:
     * - Poziva se [AuthRepositoryImplementation.registerUser]
     *
     * THEN:
     * - Rezultat je [BaseResponse.ErrorResponse]
     * - Poruka je "Neuspešna registracija"
     */
    @Test
    fun registerUser_neuspesnaRegistracijaServerVrationull()=runTest{
        val request= RegisterRequest(
            "test@test.com", "password123",
            fullName ="Test test" ,
            role = Role.ROLE_DOCTOR,
            specialization ="Kardiolog",
            hospital = null,
            maxPatients = 20,
            isGeneral = false,
            selectedDoctor = null
        )

        coEvery { authService.findUserByEmail(request.email) }returns null
        coEvery { authService.registerUser(request) }returns null
        val result=authRepository.registerUser(request)
        assertTrue { result is BaseResponse.ErrorResponse }
        assertEquals("Neuspešna registracija" ,(result as BaseResponse.ErrorResponse).message)
        coVerify(exactly = 1) { authService.findUserByEmail(request.email) }
        coVerify(exactly = 1) { authService.registerUser(request) }
        confirmVerified(authService)

    }

    /**
     * Testira se scenario uspešnog ulogovanja
     *
     * GIVEN
     * -Već postojeći korisnik treba da se uloguje
     * -AuthService vraća [AuthResponse]
     *
     * -WHEN
     * -Poziva se [AuthRepositoryImplementation.loginUser]
     *
     *-THEN
     * -Rezultat je [BaseResponse.SuccessResponse]
     * -Poruka je "Korisnik ulogovan"
     */
    @Test
    fun loginUser_uspesnoLogovanje()=runTest{
        val request= LoginRequest(
            email = "test@test.com",
            password = "password123"
        )
        val response= AuthResponse(
            userId ="uuid-1",
            email = "test@test.com",
            role = Role.ROLE_PATIENT,
            token = "nviohp111"
        )
        coEvery { authService.loginUser(request) }returns response
        val result=authRepository.loginUser(request)
        assertTrue { result is BaseResponse.SuccessResponse }
        assertEquals("Korisnik ulogovan",(result as BaseResponse.SuccessResponse).message)
        assertEquals(response,(result as BaseResponse.SuccessResponse).data)
        coVerify(exactly = 1) {authService.loginUser(request)  }
        confirmVerified(authService)
    }
    /**
     * Testira se scenario neuspešnog ulogovanja
     *
     * GIVEN
     * -Već postojeći korisnik treba da se uloguje
     * -AuthService vraća null
     *
     * -WHEN
     * -Poziva se [AuthRepositoryImplementation.loginUser]
     *
     *-THEN
     * -Rezultat je [BaseResponse.ErrorResponse]
     * -Poruka je "Neuspešno ulogovanje"
     */
    @Test
    fun loginUser_neuspesnoLogovanje()=runTest{
        val request= LoginRequest(
            email = "test@test.com",
            password = "password123"
        )

        coEvery { authService.loginUser(request) }returns null
        val result=authRepository.loginUser(request)
        assertTrue { result is BaseResponse.ErrorResponse }
        assertEquals("Neuspešno ulogovanje",(result as BaseResponse.ErrorResponse).message)
        coVerify(exactly = 1) {authService.loginUser(request)  }
        confirmVerified(authService)
    }
}