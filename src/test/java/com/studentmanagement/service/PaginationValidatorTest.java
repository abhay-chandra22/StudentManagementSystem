package com.studentmanagement.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaginationValidatorTest {
    private final PaginationValidator validator = new PaginationValidator();

    @Test
    void shouldAcceptValidParameters(){
        assertEquals(PaginationValidationResult.VALID, validator.validate(0,10,"id","asc"));
    }

    @Test
    void shouldRejectNegativePage(){
        assertEquals(PaginationValidationResult.INVALID_PAGE , validator.validate(-1,10,"id","asc"));
    }

    @Test
    void shouldRejectInvalidSize(){
        assertEquals(PaginationValidationResult.INVALID_SIZE , validator.validate(0,0,"id","asc"));
    }

    @Test
    void shouldRejectInvalidSortField(){
        assertEquals(PaginationValidationResult.INVALID_SORT_FIELD , validator.validate(0,10,"password","asc"));
    }

    @Test
    void shouldRejectInvalidSortDirection(){
        assertEquals(PaginationValidationResult.INVALID_SORT_DIRECTION , validator.validate(0,10,"id","up"));
    }
}
