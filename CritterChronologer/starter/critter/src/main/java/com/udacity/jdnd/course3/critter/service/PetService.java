package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerService customerService;

    public List<Pet> getPetsByCustomerId(Long customerId){
        return petRepository.findByCustomer_Id(customerId);
    }

    public Pet savePet(Pet pet) {
        Pet savedPet =  petRepository.save(pet);
        customerService.get(savedPet.getCustomer().getId()).addPet(pet);
        return pet;
    }

    public Pet getPet(long petId) {
        return petRepository.getOne(petId);
    }

    public List<Pet> getPetsByOwner(long ownerId) {
        return petRepository.findByCustomer_Id(ownerId);
    }
    public Pet updatePet(Pet pet){
        Pet oldPet = petRepository.getOne(pet.getId());
        BeanUtils.copyProperties(pet, oldPet);
        return petRepository.save(oldPet);
    }
}
