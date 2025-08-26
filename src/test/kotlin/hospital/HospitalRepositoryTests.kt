package com.example.hospital

import com.example.domain.Hospital
import com.example.hospital_admin.repository.HospitalRepository
import com.example.hospital_admin.repository.HospitalRepositoryImplementation
import com.example.hospital_admin.service.HospitalService
import com.example.hospital_admin.service.HospitalServiceImplementation
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
 * Testovi koji služe za proveru operacija sa bolnicama
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
class HospitalRepositoryTests {

    private lateinit var service: HospitalService
    private lateinit var repository: HospitalRepository

    /**
     * Izvršava se pre svakog testa u klasi
     * Kreira mock za HospitalService koristeći MockK
     * Inicijalizuje AuthRepositoryImplementation sa mockovanim HospitalService
     */

    @BeforeEach
    fun setUp(){
        service= mockk()
        repository= HospitalRepositoryImplementation(service)

    }

    /**
     * Test za metodu za dodavanje nove bolnice
     *
     * -GIVEN:
     * -Nova bolnica
     * -Podaci ispravno uneseni
     * -HospitalService vraća validan odgovor
     *
     * WHEN:
     * - Poziva se [HospitalRepositoryImplementation.addHospital]
     *
     * -THEN
     * - Rezultat je [BaseResponse.SuccessResponse]
     * -Poruka je "Bolnica uspešno dodata"
     * -Podaci u odgovoru su isti kao mockovani [Hospital]
     */
    @Test
    fun addHospital_uspesnoDodavanje()= runTest {
        val hospital= Hospital(

            name ="Opsta bolnica Čačak",
            address = "Miloša Obrenovića 12",
            city = "Čačak"
        )
        val response=Hospital(
            name ="Opsta bolnica Čačak",
            address = "Miloša Obrenovića 12",
            city = "Čačak"
        )

        coEvery { service.addHospital(hospital) } returns response
        val result=repository.addHospital(hospital)
        assertTrue { result is BaseResponse.SuccessResponse }
        assertEquals(response,(result as BaseResponse.SuccessResponse).data)
        assertEquals("Bolnica uspešno dodata",(result as BaseResponse.SuccessResponse).message)
        coVerify(exactly = 1) {service.addHospital(hospital)  }
        confirmVerified(service)
    }
    /**
     * Test za metodu za dodavanje nove bolnice
     *
     * -GIVEN:
     * -Nova bolnica
     * -Podaci ispravno uneseni
     * -Service vraća neuspešan odgovor
     *
     * WHEN:
     * - Poziva se [HospitalRepositoryImplementation.addHospital]
     *
     * -THEN
     * - Rezultat je [BaseResponse.ErrorResponse]
     * -Poruka je "Bolnica nije dodata"
     * -Podaci u odgovoru nisu isti kao mockovani [Hospital]
     */

    @Test
    fun addHospital_neuspesnoDodavanje()=runTest{
        val hospital=Hospital(

            name ="Opsta bolnica Čačak",
            address = "Miloša Obrenovića 12",
            city = "Čačak"
        )
        coEvery { service.addHospital(hospital)}returns null
        val result=repository.addHospital(hospital)
        assertTrue { result is BaseResponse.ErrorResponse }
        assertEquals("Bolnica nije dodata",(result as BaseResponse.ErrorResponse).message)
        coVerify(exactly = 1) {service.addHospital(hospital)  }
        confirmVerified(service)


    }

    /**
     * Test za funkciju gde se vraćaju sve bolnice
     *
     * GIVEN:
     * -Lista bolnica kao simulacija servisa
     *
     * WHEN:
     * -Repository poziva getHospitals()
     *
     * THEN:
     * -Vraća se [ListResponse.SuccessResponse] sa listom bolnica
     * -Proverava se poruka "Lista bolnica pronađena"
     * -Proverava se da je servis pozvan tačno jednom
     */

    @Test
    fun getHospitals_vraceneSveBolnice()=runTest {
        val hospitals = listOf(
        Hospital(id = "1", name = "Opsta bolnica Čačak", address = "Miloša Obrenovića 12", city = "Čačak"),
        Hospital(id = "2", name = "Bolnica Beograd", address = "Kralja Petra 5", city = "Beograd")
    )
        coEvery { service.getHospitals() }returns hospitals
        val result=repository.getHospitals()
        assertTrue { result is ListResponse.SuccessResponse }
        assertEquals(hospitals,(result as ListResponse.SuccessResponse).data)
        assertEquals("Lista bolnica pronađena",(result as ListResponse.SuccessResponse).message)
        coVerify(exactly = 1) { service.getHospitals() }
        confirmVerified(service)

    }
    /**
     * Test za funkciju getHospitals() kada nema bolnica u bazi (simulacija).
     *
     * GIVEN:
     * - Prazna lista bolnica kao simulacija (mock) servisa
     * WHEN:
     * - Repository poziva service.getHospitals()
     * THEN:
     * - Vraća se ErrorResponse sa porukom "Nema bolnica"
     * - Proverava se da je servis pozvan tačno jednom
     */
    @Test
    fun getHospitals_vracenaPraznaListaBolnica()=runTest {

        coEvery { service.getHospitals() }returns emptyList()
        val result=repository.getHospitals()
        assertTrue { result is ListResponse.ErrorResponse }

        assertEquals("Nema bolnica",(result as ListResponse.ErrorResponse).message)
        coVerify(exactly = 1) { service.getHospitals() }
        confirmVerified(service)

    }
}