package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    EmployeeService employeeService;

    public Schedule createSchedule(Schedule schedule) {
        if(schedule.getCustomerSet()==null)
            schedule.setCustomerSet( new HashSet<>() );
        Set<Customer> customerSet = schedule.getCustomerSet();
        schedule.getPetList().forEach(p -> customerSet.add(p.getCustomer()));
        return scheduleRepository.save(schedule);
    }


    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        return scheduleRepository.findByEmployeeList_Id(employeeId);
    }

    public List<Schedule> getScheduleForPet(long petId) {
        return scheduleRepository.findByPetList_Id(petId);
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        return scheduleRepository.findByCustomerSet_Id(customerId);
    }
}
