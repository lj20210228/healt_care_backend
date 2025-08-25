package com.example.doctor.service

import com.example.doctor.EditDoctorRequest
import com.example.domain.Doctor
import com.example.domain.Hospital
import javax.print.Doc


/**
 * Servis za komunikaciju sa tabelom DoctorTable
 */


interface DoctorService {

    /**
     * Funkcija koja vraća sve lekare u jednoj bolnici
     *
     * @param hospitalId Id bolnice
     * @return [List[Doctor]] Lista lekara u toj bolnici
     */
    suspend fun getDoctorsInHospital(hospitalId: String?): List<Doctor?>

    /**
     * Funkcija koja vraća sve lekare opšte prakse u jednoj bolnici
     * služiće pacijentima za izbor
     *
     * @param hospitalId Id bolnice
     * @return [List[Doctor]] Lista lekara opšte prakse u jednoj bolnici
     */
    suspend fun getGeneralDoctorsInHospital(hospitalId: String?): List<Doctor?>

    /**
     * Funkcija koja ažurira podatke o lekaru na njegov zahtev, moguće je promeniti bolnicu ili
     * maksimalan broj pacijenata kojima može biti izabrani
     *
     * @param request Poziv koji sadrži bolnicu ili maksimalan broj pacijenata
     *@return [Doctor] Metoda vraća ažurirane podatke o lekaru
     *
     */
    suspend fun editDoctorProfile(request: EditDoctorRequest): Doctor?


    /**
     * Funckija koja povećava parametar currentPatients za 1, svaki put kada pacijent izabere izabranog lekara
     *
     * @return [Doctor] Metoda vraća ažurirane podatke o lekaru
     */
    suspend fun editCurrentPatients(id: String): Doctor?

    /**
     * Funkcija koja vraća sve lekare određene specijaliazcije u nekoj bolnici
     *
     * @param specialization Naziv specijalizacije lekara koji su nam potrebni
     * @param hospitalId Id bolnice u kojoj tražimo lekare
     * @return [List[Doctor]]
     */
    suspend fun getDoctorsForSpecialization(specialization: String,hospitalId: String): List<Doctor?>


}