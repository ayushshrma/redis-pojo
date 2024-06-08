package com.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    /** The main data object or List of objects. Required if there are no errors */
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiError> errors;

    /** Paging results object, only if this endpoint returns a collection */
    private ApiPaging paging;
}
