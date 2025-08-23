package com.example.hospital_admin.service

import com.example.database.DatabaseFactory
import com.example.database.HospitalTable
import com.example.domain.Hospital
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID


/**
 * Servis za podatke o bolnicama, implementacija HospitalService interfejsa
 */
class HospitalServiceImplementation: HospitalService {

    /**
     * Funckija za dodavanje bolnice
     *
     * @param hospital Bolnica koju treba dodati
     * @return [Hospital] Podaci o dodatoj bolnici
     */
    override suspend fun addHospital(hospital: Hospital): Hospital? {
        var statement: InsertStatement<Number>? =null
        var hospitalId: UUID?=null
        DatabaseFactory.dbQuery {
            statement=HospitalTable.insert {
                it[name]=hospital.name
                it[address]=hospital.address
                it[city]=hospital.city
            }
            hospitalId=statement.resultedValues?.get(0)?.get(HospitalTable.id)
        }
        return if(hospitalId!=null){
            val hospitalAdded= DatabaseFactory.dbQuery {
                HospitalTable.selectAll().where{
                    HospitalTable.id eq hospitalId!!
                }.singleOrNull()
            }
            rowToHospital(hospitalAdded)
        } else null

    }

    /**
     * Funkcija za pronalaženje svih bolnica
     *
     * @return [List[Hospital]] Lista svih pronaenih bolnica
     */
    override suspend fun getHospitals(): List<Hospital?> {
        return DatabaseFactory.dbQuery {
            HospitalTable.selectAll().map {
                rowToHospital(it)
            }
        }
    }

    /**
     * Funkcija za pretvaranje reda tabele u objekat Hospital
     *
     * @param row Red u tabeli
     * @return [Hospital] Objekat Hospital
     */
    private fun rowToHospital(row: ResultRow?): Hospital?{
        return if (row!=null){
            Hospital(
                id = row[HospitalTable.id].toString(),
                name = row[HospitalTable.name],
                address = row[HospitalTable.address],
                city = row[HospitalTable.city]
            )

        }else null
    }
}