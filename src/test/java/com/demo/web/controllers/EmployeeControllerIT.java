package com.demo.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.common.AbstractIntegrationTest;
import com.demo.entities.Employee;
import com.demo.repositories.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class EmployeeControllerIT extends AbstractIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private List<Employee> employeeList = null;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAllInBatch();
        employeeList = new ArrayList<>();
        Employee obj = new Employee();
        employeeList.add(obj);
        employeeList = employeeRepository.saveAll(employeeList);
    }

    @Test
    void shouldFetchAllEmployees() throws Exception {
        this.mockMvc
                .perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()", is(employeeList.size())))
                .andExpect(jsonPath("$.paging.totalElements", is(1)))
                .andExpect(jsonPath("$.paging.pageNumber", is(0)))
                .andExpect(jsonPath("$.paging.totalPages", is(1)));
    }

    @Test
    void shouldFindEmployeeById() throws Exception {
        Employee employee = employeeList.get(0);
        UUID employeeId = employee.getId();

        this.mockMvc
                .perform(get("/employee/{id}", employeeId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employee.getId().toString())));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        Employee employee = employeeList.get(0);
        this.mockMvc
                .perform(delete("/employee/{id}", employee.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employee.getId().toString())));
        this.mockMvc
                .perform(delete("/employee/{id}", employee.getId().toString()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors[0].code", is("ENTITY_NOT_FOUND")));
    }
}
