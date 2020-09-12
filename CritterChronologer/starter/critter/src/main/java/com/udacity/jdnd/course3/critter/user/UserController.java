package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertCustomerDtoToEntry(customerDTO);
        customerDTO = convertCustomerEntryToDTO(customerService.save(customer));
        if(customerDTO.getId() < 0) throw new RuntimeException("There was error during saving");
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return convertCustomerEntryToDTOList(customerService.getAll());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return convertCustomerEntryToDTO(customerService.getOwnerByPet(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        convertEmployeeDtoToEntry(employeeDTO);
        employeeDTO = convertEmployeeEntryToDto(employeeService.save(convertEmployeeDtoToEntry(employeeDTO)));
        if(employeeDTO.getId() < 0) throw new RuntimeException("There was error during saving");
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        EmployeeDTO employeeDTO = convertEmployeeEntryToDto(employeeService.getEmployee(employeeId));
        if(employeeDTO.getId() != employeeId) throw new RuntimeException("There was error during getting employee");
        return employeeDTO;
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        return convertEmployeeEntryToDtoList(employeeService.findEmployeesForService(employeeDTO.getDate().getDayOfWeek(), employeeDTO.getSkills()));
    }

    public List<CustomerDTO> convertCustomerEntryToDTOList(List<Customer> customerList){
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerList.forEach( c -> customerDTOList.add(convertCustomerEntryToDTO(c)));
        return customerDTOList;
    }
    public CustomerDTO convertCustomerEntryToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        List<Long> petIds = new ArrayList<>();
        if( customer.getPets()!=null) {
            customer.getPets().forEach(pet -> petIds.add(pet.getId()));
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }
    public Customer convertCustomerDtoToEntry(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        List<Pet> petList = petService.getPetsByCustomerId(customerDTO.getId());
        customer.setPets(petList);
        return customer;
    }
    public Employee convertEmployeeDtoToEntry(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        return employee;
    }
    public EmployeeDTO convertEmployeeEntryToDto(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }
    public List<EmployeeDTO> convertEmployeeEntryToDtoList(List<Employee> employeeList){
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeList.forEach(e->employeeDTOList.add(convertEmployeeEntryToDto(e)));
        return employeeDTOList;
    }
}
