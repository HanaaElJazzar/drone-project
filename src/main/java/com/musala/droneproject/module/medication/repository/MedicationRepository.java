package com.musala.droneproject.module.medication.repository;

import com.musala.droneproject.module.medication.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/*
    Added by: Hanaa ElJazzar
    Date: 06/05/2023
*/
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    @Query(value = "SELECT m.drone_id FROM MEDICATION m where m.CODE = ?1 and m.DRONE_ID is not null", nativeQuery = true)
    public Long findDroneOfMedication(String medication_code);

    public Medication findMedicationByCode(String code);
}
