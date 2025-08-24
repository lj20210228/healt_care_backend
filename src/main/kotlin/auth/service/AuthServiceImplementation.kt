package com.example.auth.service

import com.example.auth.AuthResponse
import com.example.auth.LoginRequest
import com.example.auth.RegisterRequest
import com.example.auth.Role
import com.example.database.DatabaseFactory
import com.example.database.DoctorTable
import com.example.database.HospitalTable
import com.example.database.PatientTable
import com.example.database.UserTable
import com.example.domain.User
import com.example.security.JwtConfig
import com.example.security.hashPassword
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll


import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

/**
 * Servis za autentikaciju
 *
 * @param jwtService Parametar za Jwt servis pomocu koga ce se kreirati token
 */


class AuthServiceImplementation(private val jwtService: JwtConfig): AuthService {


    /**
     *
     * Funkcija za registrovanje korisnika
     *
     * @param params [RegisterRequest] Podaci o korisniku
     * @return [AuthResponse] odgovor sa servera(podaci o korisniku)
     */
    override suspend fun registerUser(params: RegisterRequest): AuthResponse? {
        var statement: InsertStatement<Number>? =null
        var userId: UUID?=null

        println("User $params")





        //Funkcija za dodavanje korisnika u UserTable
        DatabaseFactory.dbQuery {
            statement=UserTable.insert {
                it[email]=params.email
                it[password]= hashPassword(params.password)
                it[role]=params.role
            }
            userId=statement.resultedValues?.get(0)?.get(UserTable.id)

        }
        when(params.role){
            Role.ROLE_DOCTOR -> {
                //Ako je korisnik doktor dodaje se u DoctorTable
                DatabaseFactory.dbQuery {
                    DoctorTable.insert {
                        it[fullName]=params.fullName
                        it[specialization]=params.specialization!!
                        it[maxPatients]=params.maxPatients
                        it[DoctorTable.hospitalId]= UUID.fromString(params.hospital)
                        it[isGeneral]=params.isGeneral?:false

                    }
                }
            }
            Role.ROLE_PATIENT -> {
                //Ako je korisnik Patient dodaje se u PatientTable
                val hospitalId = params.hospital?.let { UUID.fromString(it) }

                DatabaseFactory.dbQuery {
                    PatientTable.insert{
                        it[fullName]=params.fullName
                        it[selectedDoctor]=if (params.selectedDoctor!=null) UUID.fromString(params.selectedDoctor) else null
                        it[PatientTable.hospitalId]= hospitalId

                    }
                }
            }

        }

        //Generisanje tokena
        val token=jwtService.createAccessToken(userId.toString())

        //Pronalazak dodatog korisnika u UserTable
        val user= DatabaseFactory.dbQuery {
            UserTable.selectAll().where{ UserTable.id eq userId!! }.singleOrNull()
        }
        val response= rowToResponse(user)

        return  response?.copy(token=token)









    }


    /**
     * Funkcija za logovanje
     *
     * @param params [LoginRequest] parametri za logovanje
     * @return [AuthResponse] Odgovor sa servera(podaci o korisniku)
     */
    override suspend fun loginUser(params: LoginRequest): AuthResponse? {
        val hashedPass=hashPassword(params.password)

        //Provera da li korisnik postoji u bazi
        val user= DatabaseFactory.dbQuery {
            UserTable.select(UserTable.email eq params.email and ( UserTable.password eq hashedPass)).firstOrNull()

        }
        val userResponse=rowToResponse(user)
        val token = jwtService.createAccessToken(userResponse?.userId!!)
        return userResponse.copy(token=token)
    }

    /**
     * Funkcija za proveru da li mejl vec postoji u bazi
     *
     * @param email Email korisnika koji se registruje
     * @return [User] Povratna vrednost ukoliko korisnik postoji, ako ne onda je null
     */
    override suspend fun findUserByEmail(email: String): User? {
        println("Email $email")
        val isEmailExist= DatabaseFactory.dbQuery {
            UserTable.selectAll().where { UserTable.email eq email }.singleOrNull()


        }
        println("IsEmailExist $isEmailExist")
        return rowToUser(isEmailExist)
    }


    /**
     * Funkcija koja pretvara odgovor iz baze u AuthResponse objekat
     *
     * @param row [ResultRow] rezultat SQL upita
     * @return [AuthResponse] Odgovor pretvoren u AuthResponse objekat
     */
    private fun rowToResponse(row: ResultRow?): AuthResponse?{
        return if(row==null)
            null
        else AuthResponse(
            userId = row[UserTable.id].toString(),
            email = row[UserTable.email],
            role = row[UserTable.role],
        )
    }
    /**
     * Funkcija koja pretvara odgovor iz baze u User objekat
     *
     * @param row [ResultRow] rezultat SQL upita
     * @return [AuthResponse] Odgovor pretvoren u User objekat
     */
    private fun rowToUser(row: ResultRow?): User?{
        println("Row $row")
        return if(row==null)
            null
        else User(
            email = row[UserTable.email],
            password = row[UserTable.password],
            role = row[UserTable.role],
        )
    }
}

