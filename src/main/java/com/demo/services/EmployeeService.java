package com.demo.services;

import com.demo.dto.EmployeeDTO;
import com.demo.entities.Employee;
import com.demo.enums.ErrorCode;
import com.demo.exception.AppException;
import com.demo.mapper.EmployeeMapper;
import com.demo.repositories.EmployeeRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.hibernate.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.demo.utils.AppConstants.EMPLOYEE;
import static com.demo.utils.AppConstants.EMPLOYEE_LIST;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeeMapper employeeMapper;

    @Autowired
    private CacheService cacheService;

    public Page<EmployeeDTO> findAllEmployees(Pageable pageable) {
        List<EmployeeDTO> employeeDTOList;
        Page<Employee> employeesPage = null;
        employeeDTOList = cacheService.fetchListFromCache(EMPLOYEE_LIST,EmployeeDTO.class);
        if(isNull(employeeDTOList)){
            employeesPage = employeeRepository.findAll(pageable);
            employeeDTOList = employeeMapper.toDTOList(employeesPage.getContent());
            cacheService.addListToCache(EMPLOYEE_LIST,employeeDTOList);
            cacheService.addCacheExpireTime(EMPLOYEE_LIST,1L, TimeUnit.HOURS);
        }
        return new PageImpl<>(employeeDTOList, pageable, employeesPage.getTotalElements());
    }

    public Optional<EmployeeDTO> findEmployeeById(UUID id) {
        Map<String,Employee> employeeMap = cacheService.fetchMapFromCache(EMPLOYEE,String.class,Employee.class);
        Optional<Employee> employee = ofNullable(employeeMap.get(id.toString()));
        if(employeeMap.isEmpty()){
            employee =  employeeRepository.findById(id);
            employee.ifPresent(value -> employeeMap.put(id.toString(), value));
            cacheService.addMapToCache(EMPLOYEE,employeeMap);
            cacheService.addCacheExpireTime(EMPLOYEE,1L,TimeUnit.HOURS);
        }
        return employee.map(employeeMapper::toDTO);
    }

    @Transactional
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        // Update the employee object with data from employeeDTO
        employeeMapper.mapEmployeeWithDTO(employee, employeeDTO);
        // Save the updated employee object
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }

    @Transactional
    public void deleteEmployeeById(UUID id) {
        employeeRepository.deleteById(id);
    }
}
