package com.musala.droneproject.module.medication.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/*
    Added by: Hanaa ElJazzar
    Date: 06/05/2023
    Medication table/entity added to handle Medications
*/
@Entity
@Table(name = "MEDICATION")
@Setter @Getter
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Medication name cannot be null.")
    @Column(name = "name")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Medication Name can only contain letters, numbers, '-' and '_'")
    private String name;

    @Min(value = 0, message = "Medication weight cannot be less than 0.")
    @NotNull(message = "Medication weight has to be provided.")
    @Valid
    private Double weight;

    @NotNull(message = "Medication code cannot be null.")
    @Pattern(regexp = "^[A-Z_0-9]*$", message = "Code can only contain upper case letters, numbers, and '_'")
    @Column(unique = true)
    private String code;

    @Lob
    @Column(name = "image", nullable = true)
    private byte[] image;

    public Medication() {
    }

    public Medication(String name, Double weight, String code, byte[] image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }
}
