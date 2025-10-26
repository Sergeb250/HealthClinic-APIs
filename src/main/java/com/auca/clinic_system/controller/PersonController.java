package com.auca.clinic_system.controller;

import com.auca.clinic_system.dto.PersonDTO;
import com.auca.clinic_system.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    
    @Autowired
    private PersonService personService;
    
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy) {
        
        if (page != null && size != null) {
            Sort sort = sortBy != null ? Sort.by(sortBy) : Sort.by("id");
            Page<PersonDTO> persons = personService.getAllPersons(PageRequest.of(page, size, sort));
            return ResponseEntity.ok(persons.getContent());
        }
        
        return ResponseEntity.ok(personService.getAllPersons());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/province/{provinceName}")
    public ResponseEntity<List<PersonDTO>> getPersonsByProvince(@PathVariable String provinceName) {
        return ResponseEntity.ok(personService.getPersonsByProvinceName(provinceName));
    }
    
    @GetMapping("/province/code/{provinceCode}")
    public ResponseEntity<List<PersonDTO>> getPersonsByProvinceCode(@PathVariable String provinceCode) {
        return ResponseEntity.ok(personService.getPersonsByProvinceCode(provinceCode));
    }
    
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        PersonDTO savedPerson = personService.createPerson(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        return personService.updatePerson(id, personDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (personService.deletePerson(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
