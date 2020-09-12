package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    EmployeeService employeeService;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }


    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);
        List<Schedule> list = getAllSchedules();
        return scheduleRepository.findByEmployeeList_Id(employeeId);
        //return scheduleRepository.findByEmployeeList(employee);
    }
}
