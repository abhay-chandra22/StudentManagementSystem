package com.studentmanagement.service;

import org.springframework.stereotype.Component;

@Component
public class PaginationValidator {

    public PaginationValidationResult validate(int page , int size , String sortBy , String sortDir){
        if(page < 0){
            return PaginationValidationResult.INVALID_PAGE;
        }

        if(size <= 0){
            return PaginationValidationResult.INVALID_SIZE;
        }

        if(!sortBy.equals("id") && !sortBy.equals("name") && !sortBy.equals("email") && !sortBy.equals("age") && !sortBy.equals("course")){
            return PaginationValidationResult.INVALID_SORT_FIELD;
        }

        if(!sortDir.equals("asc") && !sortDir.equals("desc")){
            return PaginationValidationResult.INVALID_SORT_DIRECTION;
        }

        return PaginationValidationResult.VALID;
    }
}
