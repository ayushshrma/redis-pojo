package com.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

import com.demo.dto.EmployeeDTO;
import com.demo.entities.Employee;
import com.demo.mapper.EmployeeMapper;
import com.demo.repositories.EmployeeRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void findEmployeeById() {
        // given
        given(employeeRepository.findById(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9")))
                .willReturn(Optional.of(getEmployee()));
        given(employeeMapper.toDTO(any(Employee.class))).willReturn(getEmployeeDTO());
        // when
        Optional<EmployeeDTO> optionalEmployee = employeeService.findEmployeeById(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9"));
        // then
        assertThat(optionalEmployee).isPresent();
        EmployeeDTO Employee = optionalEmployee.get();
        assertThat(Employee.getId().toString()).isEqualTo("b83d1f2a-8b1c-46b3-a871-402fdb3636f9");
    }

    @Test
    void deleteEmployeeById() {
        // given
        willDoNothing().given(employeeRepository).deleteById(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9"));
        // when
        employeeService.deleteEmployeeById(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9"));
        // then
        verify(employeeRepository, times(1)).deleteById(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9"));
    }

    private Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9"));
        return employee;
    }

    private EmployeeDTO getEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(UUID.fromString("b83d1f2a-8b1c-46b3-a871-402fdb3636f9"));
        return employeeDTO;
    }
}
