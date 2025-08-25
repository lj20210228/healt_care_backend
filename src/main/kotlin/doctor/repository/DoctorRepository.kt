package com.example.doctor.repository

import com.example.doctor.EditDoctorRequest
import com.example.domain.Doctor
import com.example.response.BaseResponse
import com.example.response.ListResponse

/**
 * Interfejs repozitorijum za metode koje služe za komunikaciju sa DoctorTable preko DoctorService
 */

interface DoctorRepository {
    /**
     * Funkcija koja vraća sve lekare u jednoj bolnici
     *
     * @param hospitalId Id bolnice
     * @return [ListResponse[Doctor]] Odgovor koji sadrži listu lekara u toj bolnici, ako je postoje, ili poruku o grešci
     */
    suspend fun getDoctorsInHospital(hospitalId: String?): ListResponse<Doctor>

    /**
     * Funkcija koja vraća sve lekare opšte prakse u jednoj bolnici
     * služiće pacijentima za izbor
     *
     * @param hospitalId Id bolnice
     * @return [ListResponse[Doctor]] Lista lekara opšte prakse u jednoj bolnici, ako postoje, ili poruku o grešci
     */
    suspend fun getGeneralDoctorsInHospital(hospitalId: String?): ListResponse<Doctor?>

    /**
     * Funkcija koja ažurira podatke o lekaru
     *
     * @param request Poziv koji sadrži bolnicu ili maksimalan broj pacijenata
     *@return [BaseResponse[Doctor]] Metoda vraća ažurirane podatke o lekaru, ako je ažuriranje uspešno, ili poruku o grešci
     *
     */
    suspend fun editDoctorProfile(request: EditDoctorRequest): BaseResponse <Doctor?>


    /**
     * Funckija koja povećava parametar currentPatients za 1, svaki put kada pacijent izabere izabranog lekara
     *
     * @return [BaseResponse[Doctor]] Metoda vraća podatke o lekaru koji je ažuriran,
     * ako je ažiriranje uspešno, ili poruku o grešci
     */
    suspend fun editCurrentPatients(id: String?): BaseResponse<Doctor?>

    /**
     * Funkcija koja vraća podatke o svim lekarima određene specijalizacije u nekoj bolnici
     *
     * @param specialization Naziv specijalizacije
     * @param hospitalId Id bolnice
     * @return [ListResponse[Doctor]] Vraća se odgovor sa servera koji sadrži podakte o lekarima ili poruku o grešci
     */
    suspend fun getDoctorsForSpecialization(specialization: String?,hospitalId: String?): ListResponse<Doctor?>

}