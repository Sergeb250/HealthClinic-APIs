package com.auca.clinic_system.service;

import com.auca.clinic_system.dto.LocationDTO;
import com.auca.clinic_system.dto.PersonDTO;
import com.auca.clinic_system.entity.*;
import com.auca.clinic_system.repository.PersonRepository;
import com.auca.clinic_system.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private VillageRepository villageRepository;
    
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<PersonDTO> getAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public Optional<PersonDTO> getPersonById(Long id) {
        return personRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<PersonDTO> getPersonsByProvinceName(String provinceName) {
        return personRepository.findByVillage_Cell_Sector_District_Province_Name(provinceName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<PersonDTO> getPersonsByProvinceCode(String provinceCode) {
        return personRepository.findByVillage_Cell_Sector_District_Province_Code(provinceCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = convertToEntity(personDTO);
        Person savedPerson = personRepository.save(person);
        return convertToDTO(savedPerson);
    }
    
    public Optional<PersonDTO> updatePerson(Long id, PersonDTO personDTO) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(personDTO.getName());
                    existingPerson.setEmail(personDTO.getEmail());
                    existingPerson.setPhone(personDTO.getPhone());
                    
                    // Update village if location is provided
                    if (personDTO.getLocation() != null && personDTO.getLocation().getVillageId() != null) {
                        villageRepository.findById(personDTO.getLocation().getVillageId())
                                .ifPresent(existingPerson::setVillage);
                    }
                    
                    Person updatedPerson = personRepository.save(existingPerson);
                    return convertToDTO(updatedPerson);
                });
    }
    
    public boolean deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public PersonDTO convertToDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setEmail(person.getEmail());
        dto.setPhone(person.getPhone());
        
        // Convert village to location hierarchy
        if (person.getVillage() != null) {
            dto.setLocation(convertVillageToLocation(person.getVillage()));
        }
        
        return dto;
    }
    
    public Person convertToEntity(PersonDTO dto) {
        Person person = new Person();
        person.setName(dto.getName());
        person.setEmail(dto.getEmail());
        person.setPhone(dto.getPhone());
        
        // Set village if location is provided
        if (dto.getLocation() != null && dto.getLocation().getVillageId() != null) {
            villageRepository.findById(dto.getLocation().getVillageId())
                    .ifPresent(person::setVillage);
        }
        
        return person;
    }
    
    private LocationDTO convertVillageToLocation(Village village) {
        LocationDTO location = new LocationDTO();
        location.setVillageId(village.getId());
        location.setVillageName(village.getName());
        
        if (village.getCell() != null) {
            Cell cell = village.getCell();
            location.setCellId(cell.getId());
            location.setCellName(cell.getName());
            
            if (cell.getSector() != null) {
                Sector sector = cell.getSector();
                location.setSectorId(sector.getId());
                location.setSectorName(sector.getName());
                
                if (sector.getDistrict() != null) {
                    District district = sector.getDistrict();
                    location.setDistrictId(district.getId());
                    location.setDistrictName(district.getName());
                    
                    if (district.getProvince() != null) {
                        Province province = district.getProvince();
                        location.setProvinceId(province.getId());
                        location.setProvinceName(province.getName());
                    }
                }
            }
        }
        
        return location;
    }
}
