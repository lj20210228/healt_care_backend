package com.example.doctor.repository

import com.example.doctor.EditDoctorRequest
import com.example.doctor.service.DoctorService
import com.example.domain.Doctor
import com.example.response.BaseResponse
import com.example.response.ListResponse

/**
 *
 * Repozitorijum koji implementira metode interfejsa DoctorRepository, i šalje povratne informacije o uspehu metoda
 *
 */

class DoctorRepositoryImplementation(val service: DoctorService): DoctorRepository {

    /**
     * Metoda koja vraća sve lekare u jednoj bolnici
     *
     * @param hospitalId Id bolnice, ukoliko je null, fukncija se prekida i vraća grešku
     * @return [ListResponse[Doctor]]] Ukoliko metoda service.getDoctorsInHosptal vrati praznu listu, vraća se greška jer u toj bolnici ne postoje lekari,
     * ukoliko ne vraća se lista svih lekara
     */
    override suspend fun getDoctorsInHospital(hospitalId: String?): ListResponse<Doctor>{
        return if (hospitalId==null)
             ListResponse.ErrorResponse(message = "Nisu uneti ispravni podaci o bolnici")
        else{
            val result=service.getDoctorsInHospital(hospitalId)
            if (result.isNotEmpty()){
                ListResponse.SuccessResponse(data = result, message = "Podaci o lekarima uspešno pronađeni")
            }else
                ListResponse.ErrorResponse(message = "Ne postoje lekari u ovoj bolnici")
        }
    }
    /**
     * Metoda koja vraća sve lekare opšte prakse u jednoj bolnici
     *
     * @param hospitalId Id bolnice, ukoliko je null, fukncija se prekida i vraća grešku
     * @return [ListResponse[Doctor]]] Ukoliko metoda service.getGeneralDoctorsInHosptal vrati praznu listu, vraća se greška jer u toj bolnici ne postoje lekari opšte prakse,
     * ukoliko ne vraća se lista svih lekara opšte prake
     */

    override suspend fun getGeneralDoctorsInHospital(hospitalId: String?): ListResponse<Doctor?> {
        return if (hospitalId==null)
            ListResponse.ErrorResponse(message = "Nisu uneti ispravni podaci o bolnici")
        else{
            val result=service.getGeneralDoctorsInHospital(hospitalId)
            if (result.isNotEmpty()){
                ListResponse.SuccessResponse(data = result, message = "Podaci o lekarima opšte prakse uspešno pronađeni")
            }else
                ListResponse.ErrorResponse(message = "Ne postoje lekari opšte prakse u ovoj bolnici")
        }
    }

    /**
     * Funkcija koja menja profil lekara na njegov zahtev
     *
     * @param request Podaci o lekaru koje treba izmeniti
     * @return [BaseResponse[Doctor]] Ukoliko rezultat metode service.editDoctorProfile vrati null, poslaće se poruka o grešci jer podaci nisu uspešno ažurirani,
     * a ukoliko se vrati objekat Doctor, vraća se ažurirani objekat i poruka o usoešnom ažuriranju
     */
    override suspend fun editDoctorProfile(request: EditDoctorRequest): BaseResponse<Doctor?> {
        val result=service.editDoctorProfile(request)
        return if(result!=null){
            BaseResponse.SuccessResponse(data = result, message = "Podaci o lekaru su uspešno ažurirani")
        }else{
            BaseResponse.ErrorResponse(message = "Podaci o lekaru nisu ažurirani")
        }
    }

    /**
     * Funkcija koja povećava broj pacinata kojima je lekar izabran
     *
     * @param id Id lekara
     * @return [BaseResponse[Doctor]] Ukoliko je prosleđeni id null, vraća se poruka o grešci, ukoliko metoda service.editCurrentPatients vrati uspešan odgovor,
     * podatke prosleđujemo kroz SuccessResponse, kao i poruku o uspešnosti, a ukoliko ažuriranje nije uspelo(result je null), vraća se poruka o grešcigit
     */
    override suspend fun editCurrentPatients(id: String?): BaseResponse<Doctor?> {
        return if(id==null){
            BaseResponse.ErrorResponse(message = "Nisu prodleđeni podaci o lekaru")
        }else{
            val result=service.editCurrentPatients(id)
            if (result!=null)
                BaseResponse.SuccessResponse(data = result, message = "Uspešno ažuriran broj pacijenata")
            else BaseResponse.ErrorResponse(message = "Broj pacijenata nije uspešno ažuriran")
        }
    }
    /**
     * Metoda koja vraća sve lekare određene specijalizacije u jednoj bolnici
     *
     * @param hospitalId Id bolnice, ukoliko je null, fukncija se prekida i vraća grešku
     * @param specialization Specijalizacija čiji spisak lekara nam treba
     * @return [ListResponse[Doctor]]] Ukoliko metoda service.getDoctorsForSpecialization vrati praznu listu, vraća se greška jer u toj bolnici ne postoje lekari te specijalizacije,
     * ukoliko ne vraća se lista svih lekara te specijalizacije
     */


    override suspend fun getDoctorsForSpecialization(
        specialization: String?,
        hospitalId: String?
    ): ListResponse<Doctor?> {
        return if (hospitalId==null||specialization.isNullOrEmpty())
            ListResponse.ErrorResponse(message = "Nisu uneti ispravni podaci")
        else{
            val result=service.getDoctorsForSpecialization(specialization=specialization, hospitalId = hospitalId)
            if (result.isNotEmpty()){
                ListResponse.SuccessResponse(data = result, message = "Podaci o lekarima specijalizacije:$specialization uspešno pronađeni")
            }else
                ListResponse.ErrorResponse(message = "Ne postoje lekari specijalizacije:$specialization u ovoj bolnici")
        }
    }
}