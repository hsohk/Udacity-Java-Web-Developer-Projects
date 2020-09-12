package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleEntityToDTO(scheduleService.createSchedule(convertScheduleDTOToEntity(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertScheduleEntityToDTOList(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertScheduleEntityToDTOList(scheduleService.getScheduleForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        throw new UnsupportedOperationException();
    }

    private Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO,schedule);
        List<Employee> employeeList = new ArrayList<>();
        List<Pet> petList = new ArrayList<>();
        scheduleDTO.getPetIds().forEach(petId->petList.add( petService.getPet(petId)));
        scheduleDTO.getEmployeeIds().forEach(employeeId->employeeList.add(employeeService.getEmployee(employeeId)));
        schedule.setPetList(petList);
        schedule.setEmployeeList(employeeList);
        return schedule;
    }

    private ScheduleDTO convertScheduleEntityToDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();
        schedule.getEmployeeList().forEach(entity->employeeIds.add(entity.getId()));
        schedule.getPetList().forEach(entity->petIds.add(entity.getId()));
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        return scheduleDTO;
    }

    private List<ScheduleDTO> convertScheduleEntityToDTOList(List<Schedule> scheduleList){
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for(Schedule schedule:scheduleList){
            scheduleDTOList.add(convertScheduleEntityToDTO(schedule));
        }
        return scheduleDTOList;
    }
}
