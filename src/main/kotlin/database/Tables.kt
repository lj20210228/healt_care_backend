package com.example.database

import com.example.auth.Role
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Tabela o svim korisnicima aplikacije, koja se koristi za lakše registrovanje i ulogovanje
 *
 * @property id ID korisnika
 * @property email Email korisnika
 * @property password Lozinka korisnika(u bazi će se čuvati heširana!)
 * @property role Uloga korisnika u sistemu(lekar ili pacijent)
 */
object UserTable: Table("users")
{

    val id=uuid("id").autoGenerate()
    override val primaryKey= PrimaryKey(id)
    val email=varchar("email",256)
    val password=text("password")
    val role=enumeration<Role>("role")
}

/**
 * Tabela u kojoj se čuvaju podaci o lekaru
 *
 * @property id Id lekara
 * @property fullName Ime i prezime lekara
 * @property specialization Specijalizacija lekara
 * @property hospitalId Spoljni ključ ka tabeli HospitalTable, tj Id bolnice kojoj lekar pripada,
 * prilikom brisanja bolnice iz tabele HospitalTable brišu se i podaci iz ove kolone
 *
 * @property maxPatients Maksimalan broj pacijenata kojima može biti izabrani lekar
 * @property currentPatients Trenutni broj pacijenata kojima je izabrani lekar
 * @property isGeneral Da li je lekar opšte prakse
 */
object DoctorTable: Table("doctor"){
    val id=uuid("id").autoGenerate()
    override val primaryKey= PrimaryKey(id)
    val fullName=varchar("full_name",256)
    val specialization=varchar("specialization",256)
    val hospitalId=reference("hospital_id", HospitalTable.id, onDelete = ReferenceOption.CASCADE).nullable()
    val maxPatients=integer("max_patients").nullable()
    val currentPatients=integer("current_patients")
    val isGeneral=bool("is_general")

}
/**
 * Tabela gde se nalaze podaci o pacijentima
 *
 * @property id Id pacijenta
 * @property fullName Ime i prezime pacijenta
 * @property selectedDoctor Id izabranog lekara, spoljni ključ ka tabeli DoctorTable,
 * prilikom brisanja lekara iz tabele DoctorTable briše se i id iz ove kolone
 *
 * @property hospitalId Id bolnice , spoljni ključ ka tabeli HospitalTable,
 * gde se pacijent leči ,brisanje isto kao za doktora
 */
object PatientTable: Table("patient"){
    val id=uuid("id").autoGenerate()
    override val primaryKey= PrimaryKey(id)
    val fullName=varchar("full_name",256)
    val selectedDoctor=reference("doctor_id", DoctorTable.id, onDelete = ReferenceOption.CASCADE).nullable()
    val hospitalId=reference("hospital_id", HospitalTable.id, onDelete = ReferenceOption.CASCADE).nullable()
}


/**
 * Tabela u kojoj se nalaze podaci o bolnicama
 *
 * @property id Id bolnice
 * @property name Ime bolnice
 * @property address Adresa bolnice
 * @property city Grad u kome se bolnica nalazi
 */
object HospitalTable: Table("hospital"){
    val id=uuid("id").autoGenerate()
    override val primaryKey= PrimaryKey(id)
    val name=text("name")
    val address=varchar("address",256)
    val city=varchar("city",256)
}