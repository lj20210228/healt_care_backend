package com.example.doctor

import com.example.doctor.repository.DoctorRepository
import com.example.doctor.repository.DoctorRepositoryImplementation
import com.example.doctor.service.DoctorService
import com.example.domain.Doctor
import com.example.domain.Hospital
import com.example.response.BaseResponse
import com.example.response.ListResponse
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
 * Testovi koji služe za proveru operacija sa lekarima
 *
 *
 * Koristi [io.mockk.MockK] za mocking zavisnosti i [kotlinx.coroutines.test.runTest]
 *  za testiranje korutina.
 *
 *
 *-GIVEN- početni uslovi testa
 *-WHEN - akcija koja se testira
 *-THEN - očekivani ishod
 */
class DoctorRepositoryTests {

    private lateinit var service: DoctorService
    private lateinit var repository: DoctorRepository

    /**
     * Izvršava se pre svakog testa u klasi
     * Kreira mock za HospitalService koristeći MockK
     * Inicijalizuje AuthRepositoryImplementation sa mockovanim HospitalService
     */

    @BeforeEach
    fun setUp(){
        service= mockk()
        repository= DoctorRepositoryImplementation(service)

    }

    /**
     * Testira scenario da su pronađeni svi lekari u bolnici, vraća se lista svih lekara u [ListResponse.SuccessResponse]
     */
    @Test
    fun getDoctorsInHospital_podaciUspesnoPronadjeni()= runTest{
        val hospitalId="1"
        val doctors=listOf<Doctor>(
            Doctor(
                id ="1",
                userId = "1",
                fullName = "Petar Petrovic",
                specialization ="Lekar opste prakse",
                healtCareCenter = "1",
                maxPatients = 20,
                currentPatients =3,
                isGeneral = true
            ),
            Doctor(
                id ="2",
                userId = "2",
                fullName = "Petar Jovanovic",
                specialization ="Neurolog",
                healtCareCenter = "1",
                maxPatients = 20,
                currentPatients =3,
                isGeneral = false
            ),
            Doctor(
                id ="3",
                userId = "3",
                fullName = "Milena Arsic",
                specialization ="Ginekolog",
                healtCareCenter = "1",
                maxPatients = 20,
                currentPatients =3,
                isGeneral = false
            ),
            Doctor(
                id ="4",
                userId = "4",
                fullName = "Ivana Jokic",
                specialization ="Dermatolog",
                healtCareCenter = "1",
                maxPatients = 20,
                currentPatients =3,
                isGeneral = false
            )

        )
        coEvery { service.getDoctorsInHospital(hospitalId) }returns doctors
        val result=repository.getDoctorsInHospital(hospitalId)
        assertTrue { result is ListResponse.SuccessResponse }
        assertEquals(doctors, (result as ListResponse.SuccessResponse).data)
        assertEquals("Podaci o lekarima uspešno pronađeni", (result as ListResponse.SuccessResponse).message)

        coVerify(exactly = 1) { service.getDoctorsInHospital(hospitalId) }
        confirmVerified(service)



    }

    /**
     * Testira scenario gde su podaci ispravno uneti ali nema lekara u bolnici, pa se vraća [ListResponse.ErrorResponse]
     */
    @Test
    fun getDoctorsInHospital_nemaLekaraUBolnici()= runTest{
        val hospitalId="1"

        coEvery { service.getDoctorsInHospital(hospitalId) }returns emptyList()
        val result=repository.getDoctorsInHospital(hospitalId)
        assertTrue { result is ListResponse.ErrorResponse }
        assertEquals("Ne postoje lekari u ovoj bolnici", (result as ListResponse.ErrorResponse).message)

        coVerify(exactly = 1) { service.getDoctorsInHospital(hospitalId) }
        confirmVerified(service)



    }

    /**
     * Testira se scenario gde nije poslat id bolnice, pa se vraća [ListResponse.ErrorResponse]
     */
    @Test
    fun getDoctorsInHospital_nemaIdBolnice()= runTest{

        coEvery { service.getDoctorsInHospital(null) } returns emptyList()
        val result=repository.getDoctorsInHospital(null)
        assertTrue { result is ListResponse.ErrorResponse }

        assertEquals("Nisu uneti ispravni podaci o bolnici", (result as ListResponse.ErrorResponse).message)

        coVerify(exactly = 0) { service.getDoctorsInHospital(null) }
        confirmVerified(service)



    }
    /**
     * Testira scenario da su pronađeni svi lekari opšte prakse
     * u bolnici, vraća se lista svih lekara u [ListResponse.SuccessResponse]
     */
    @Test
    fun getGeneralDoctors_podaciUspesnoPronadjeni()= runTest{
        val hospitalId="1"
        val doctors=listOf<Doctor>(
            Doctor(
                id ="1",
                userId = "1",
                fullName = "Petar Petrovic",
                specialization ="Lekar opste prakse",
                healtCareCenter = "1",
                maxPatients = 20,
                currentPatients =3,
                isGeneral = true
            ),


            )
        coEvery { service.getGeneralDoctorsInHospital(hospitalId) }returns doctors
        val result=repository.getGeneralDoctorsInHospital(hospitalId)
        assertTrue { result is ListResponse.SuccessResponse }
        assertEquals(doctors, (result as ListResponse.SuccessResponse).data)
        assertEquals("Podaci o lekarima opšte prakse uspešno pronađeni", (result as ListResponse.SuccessResponse).message)

        coVerify(exactly = 1) { service.getGeneralDoctorsInHospital(hospitalId) }
        confirmVerified(service)



    }

    /**
     * Testira scenario gde su podaci ispravno uneti ali nema lekara
     * opšte prakse u bolnici, pa se vraća [ListResponse.ErrorResponse]
     */
    @Test
    fun getGeneralDoctorsInHospital_nemaLekaraUBolnici()= runTest{
        val hospitalId="1"

        coEvery { service.getGeneralDoctorsInHospital(hospitalId) }returns emptyList()
        val result=repository.getGeneralDoctorsInHospital(hospitalId)
        assertTrue { result is ListResponse.ErrorResponse }
        assertEquals("Ne postoje lekari opšte prakse u ovoj bolnici", (result as ListResponse.ErrorResponse).message)

        coVerify(exactly = 1) { service.getGeneralDoctorsInHospital(hospitalId) }
        confirmVerified(service)



    }


    /**
     * Testira se scenario gde nije poslat id bolnice, pa se vraća [ListResponse.ErrorResponse]
     */
    @Test
    fun getGeneralDoctorsInHospital_nemaIdBolnice()= runTest{

        coEvery { service.getGeneralDoctorsInHospital(null) } returns emptyList()
        val result=repository.getGeneralDoctorsInHospital(null)
        assertTrue { result is ListResponse.ErrorResponse }

        assertEquals("Nisu uneti ispravni podaci o bolnici", (result as ListResponse.ErrorResponse).message)

        coVerify(exactly = 0) { service.getGeneralDoctorsInHospital(null) }
        confirmVerified(service)



    }

    /**
     * Testira scenario da su pronađeni svi lekari date specijalizacije
     * u bolnici, vraća se lista svih lekara u [ListResponse.SuccessResponse]
     */
    @Test
    fun getDoctorsForSpecialization_podaciUspesnoPronadjeni()= runTest{
        val hospitalId="1"
        val specialization="Neurolog"
        val doctors=listOf<Doctor>(
            Doctor(
                id ="1",
                userId = "1",
                fullName = "Petar Petrovic",
                specialization ="Neurolog",
                healtCareCenter = "1",
                maxPatients = 20,
                currentPatients =3,
                isGeneral = true
            ),


            )
        coEvery { service.getDoctorsForSpecialization(specialization,hospitalId) }returns doctors
        val result=repository.getDoctorsForSpecialization(specialization,hospitalId)
        assertTrue { result is ListResponse.SuccessResponse }
        assertEquals(doctors, (result as ListResponse.SuccessResponse).data)
        assertEquals("Podaci o lekarima specijalizacije:$specialization uspešno pronađeni", (result as ListResponse.SuccessResponse).message)

        coVerify(exactly = 1) { service.getDoctorsForSpecialization(specialization,hospitalId) }
        confirmVerified(service)



    }




    /**
     * Testira scenario gde su podaci ispravno uneti ali nema lekara
     * date specijalizacije u bolnici, pa se vraća [ListResponse.ErrorResponse]
     */
    @Test
    fun getDoctorsForSpecialization_nemaLekaraUBolnici()= runTest{
        val hospitalId="1"
        val specialization="Neurolog"
        coEvery { service.getDoctorsForSpecialization(specialization,hospitalId) }returns emptyList()
        val result=repository.getDoctorsForSpecialization(specialization,hospitalId)
        assertTrue { result is ListResponse.ErrorResponse }
        assertEquals("Ne postoje lekari specijalizacije:$specialization u ovoj bolnici", (result as ListResponse.ErrorResponse).message)

        coVerify(exactly = 1) { service.getDoctorsForSpecialization(specialization,hospitalId) }
        confirmVerified(service)



    }
    /**
     * Testira se scenario gde nije poslat id bolnice, pa se vraća [ListResponse.ErrorResponse]
     */
    @Test
    fun getDoctosForSpecialization_nemaIdBolnice()= runTest{

        val specialization="Neurolog"

        coEvery { service.getDoctorsForSpecialization(specialization,null) } returns emptyList()
        val result=repository.getDoctorsForSpecialization(specialization,null)
        assertTrue { result is ListResponse.ErrorResponse }

        assertEquals("Nisu uneti ispravni podaci", (result as ListResponse.ErrorResponse).message)

        coVerify(exactly = 0) { service.getDoctorsForSpecialization(specialization,null) }
        confirmVerified(service)



    }

    /**
     * Testira se scenario gde je uspešno ažuriran parametar [Doctor.maxPatients],
     * pa se vraća odgovor [BaseResponse.SuccessResponse]
     */
    @Test
    fun editDoctorProfile_uspesnoAzuriraniMaxBrojPacijenata()=runTest{
        val doctorRequest= EditDoctorRequest(
            userId = "1",
            maxPatients = 20,

        )
        val response= Doctor(
            id = "1",
            userId ="1",
            fullName = "Petar Petrovic",
            specialization = "Dermatolog",
            healtCareCenter = "2",
            maxPatients = 20,
            currentPatients = 0,
            isGeneral = false
        )
        coEvery { service.editDoctorProfile(doctorRequest)} returns response
        val result=repository.editDoctorProfile(doctorRequest)
        assertTrue { result is BaseResponse.SuccessResponse }
        assertEquals(response,(result as BaseResponse.SuccessResponse).data)
        assertEquals("Podaci o lekaru su uspešno ažurirani",(result as BaseResponse.SuccessResponse).message)
        coVerify(exactly = 1) { service.editDoctorProfile(doctorRequest)}
        confirmVerified(service)

    }

    /**
     * Testira se scenario gde je uspešno ažuriran parametar [Doctor.healtCareCenter],
     * pa se vraća odgovor [BaseResponse.SuccessResponse]
     */
    @Test
    fun editDoctorProfile_uspesnoAzuriraniHospitalId()=runTest{
        val doctorRequest= EditDoctorRequest(
            userId = "1",
            hospital = "2"

            )
        val response= Doctor(
            id = "1",
            userId ="1",
            fullName = "Petar Petrovic",
            specialization = "Dermatolog",
            healtCareCenter = "2",
            maxPatients = 20,
            currentPatients = 0,
            isGeneral = false
        )
        coEvery { service.editDoctorProfile(doctorRequest)} returns response
        val result=repository.editDoctorProfile(doctorRequest)
        assertTrue { result is BaseResponse.SuccessResponse }
        assertEquals(response,(result as BaseResponse.SuccessResponse).data)
        assertEquals("Podaci o lekaru su uspešno ažurirani",(result as BaseResponse.SuccessResponse).message)
        coVerify(exactly = 1) { service.editDoctorProfile(doctorRequest)}
        confirmVerified(service)

    }
    /**
     * Testira se scenario gde su uspešno ažurirani parametri [Doctor.maxPatients] i
     * [Doctor.healtCareCenter], pa se vraća odgovor [BaseResponse.SuccessResponse]
     */
    @Test
    fun editDoctorProfile_uspesnoAzuriraniMaxBrojPacijenataIHospitalId()=runTest{
        val doctorRequest= EditDoctorRequest(
            userId = "1",
            maxPatients = 20,
            hospital = "2"

            )
        val response= Doctor(
            id = "1",
            userId ="1",
            fullName = "Petar Petrovic",
            specialization = "Dermatolog",
            healtCareCenter = "2",
            maxPatients = 20,
            currentPatients = 0,
            isGeneral = false
        )
        coEvery { service.editDoctorProfile(doctorRequest)} returns response
        val result=repository.editDoctorProfile(doctorRequest)
        assertTrue { result is BaseResponse.SuccessResponse }
        assertEquals(response,(result as BaseResponse.SuccessResponse).data)
        assertEquals("Podaci o lekaru su uspešno ažurirani",(result as BaseResponse.SuccessResponse).message)
        coVerify(exactly = 1) { service.editDoctorProfile(doctorRequest)}
        confirmVerified(service)

    }

    /**
     * Testira se scenario gde nisu uspešno ažurirani podaci [Doctor.maxPatients] ili
     * [Doctor.healtCareCenter] tako da se vraća odgovor [BaseResponse.ErrorResponse]
     */
    @Test
    fun editDoctorProfile_neuspesnoAzuriranje()=runTest{
        val doctorRequest= EditDoctorRequest(
            userId = "1",
            maxPatients = 20,
            hospital = "2")

        coEvery { service.editDoctorProfile(doctorRequest)} returns null
        val result=repository.editDoctorProfile(doctorRequest)
        assertTrue { result is BaseResponse.ErrorResponse }
        assertEquals("Podaci o lekaru nisu ažurirani",(result as BaseResponse.ErrorResponse).message)
        coVerify(exactly = 1) { service.editDoctorProfile(doctorRequest)}
        confirmVerified(service)

    }

    /**

     */
    @Test
    fun editCurrentPatients_uspesnoAzuriranje()=runTest {
        val id= "1"
        val response= Doctor(
            id = "1",
            userId ="1",
            fullName = "Petar Petrovic",
            specialization = "Dermatolog",
            healtCareCenter = "2",
            maxPatients = 20,
            currentPatients = 0,
            isGeneral = false
        )
        coEvery { service.editCurrentPatients(id) }returns response
        val result=repository.editCurrentPatients(id)
        assertTrue { result is BaseResponse.SuccessResponse }
        assertEquals(response,(result as BaseResponse.SuccessResponse).data)
        assertEquals("Uspešno ažuriran broj pacijenata",(result as BaseResponse.SuccessResponse).message)
        coVerify(exactly = 1) { service.editCurrentPatients(id)}
        confirmVerified(service)

    }

    /**
     * Testira se scenario gde je parametar [Doctor.currentPatients] neuspešno promenjen,
     * pa se vraća odgovor [BaseResponse.ErrorResponse]
     */
    @Test
    fun editCurrentPatients_neuspesnoAzuriranje()=runTest {
        val id= "1"

        coEvery { service.editCurrentPatients(id) }returns null
        val result=repository.editCurrentPatients(id)
        assertTrue { result is BaseResponse.ErrorResponse }

        assertEquals("Broj pacijenata nije uspešno ažuriran",(result as BaseResponse.ErrorResponse).message)
        coVerify(exactly = 1) { service.editCurrentPatients(id)}
        confirmVerified(service)

    }

    /**
     * Testira se scenario gde nije prosleđen id lekara čiji parametar [Doctor.currentPatients]
     * treba ažurirati, pa se vraća odgovor [BaseResponse.ErrorResponse]
     */
    @Test
    fun editCurrentPatients_idJeNull()=runTest {
        val id= null

        coEvery { service.editCurrentPatients(id) }returns null
        val result=repository.editCurrentPatients(id)
        assertTrue { result is BaseResponse.ErrorResponse }

        assertEquals("Nisu prodleđeni podaci o lekaru",(result as BaseResponse.ErrorResponse).message)
        coVerify(exactly = 0) { service.editCurrentPatients(id)}
        confirmVerified(service)

    }




}