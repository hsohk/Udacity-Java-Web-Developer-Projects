package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet resultPet = petService.savePet(convertPetDtoToEntry(petDTO));
        return convertPetEntryTODTO(resultPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetEntryTODTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> petList =  petService.getPetsByOwner(ownerId);
        return convertPetEntryToDTOList(petList);
    }

    private Pet convertPetDtoToEntry(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO,pet);
        pet.setCustomer(customerService.get(petDTO.getOwnerId()));
        return pet;
    }
    private List<PetDTO> convertPetEntryToDTOList(List<Pet> petList){
        List<PetDTO> petDTOList = new ArrayList<>();
        for(Pet pet:petList){
            petDTOList.add(convertPetEntryTODTO(pet));
        }
        return petDTOList;
    }
    private PetDTO convertPetEntryTODTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet,petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    public PetDTO updatePet(PetDTO petDTO) {
        Pet pet = convertPetDtoToEntry(petDTO);
        return convertPetEntryTODTO(petService.updatePet(pet));
    }
}
