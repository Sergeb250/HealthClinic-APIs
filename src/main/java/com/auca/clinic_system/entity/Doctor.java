package com.auca.clinic_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String specialization;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", unique = true)
    private Person person;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();
    
    @ManyToMany(mappedBy = "doctors", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Patient> patients = new HashSet<>();
}
