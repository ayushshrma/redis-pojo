package com.demo.web.controllers;

import com.demo.dto.EmployeeDTO;
import com.demo.enums.ErrorCode;
import com.demo.exception.AppException;
import com.demo.response.ApiPaging;
import com.demo.response.ApiResponse;
import com.demo.services.EmployeeService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("employee")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ApiResponse<List<EmployeeDTO>> getAllEmployees(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EmployeeDTO> employeeDTOPage = employeeService.findAllEmployees(pageable);
        return new ApiResponse<>(employeeDTOPage.getContent(), null, new ApiPaging(employeeDTOPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable UUID id) {
        return employeeService
                .findEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Validated EmployeeDTO employeeDTO) {
        EmployeeDTO response = employeeService.saveEmployee(employeeDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("employee/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable UUID id, @RequestBody @Valid EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDTO> deleteEmployee(@PathVariable UUID id) {
        return employeeService
                .findEmployeeById(id)
                .map(employee -> {
                    employeeService.deleteEmployeeById(id);
                    return ResponseEntity.ok(employee);
                })
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
