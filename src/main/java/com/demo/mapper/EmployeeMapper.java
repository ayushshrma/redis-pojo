package com.demo.mapper;

import com.demo.dto.EmployeeDTO;
import com.demo.entities.Employee;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {

    public Employee toEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setPhone(employeeDTO.getPhone());
        employee.setDesignation(employeeDTO.getDesignation());
        return employee;
    }

    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setDesignation(employee.getDesignation());
        return employeeDTO;
    }

    public void mapEmployeeWithDTO(Employee employee, EmployeeDTO employeeDTO) {
        employee.setName(employeeDTO.getName());
        employee.setPhone(employeeDTO.getPhone());
        employee.setDesignation(employeeDTO.getDesignation());
    }

    public List<EmployeeDTO> toDTOList(List<Employee> employeeList) {
        return employeeList.stream().map(this::toDTO).toList();
    }
}
