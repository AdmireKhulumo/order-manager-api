package com.nordnet.order_manager.utils;

import com.nordnet.order_manager.models.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseUtilTest {

    @Test
    void Should_ReturnSuccessResponse_When_CallingSuccessWithMessageAndData() {
        String message = "Operation successful";
        String data = "Sample data";

        ResponseEntity<ApiResponse<String>> response = ApiResponseUtil.success(message, data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(data, response.getBody().getData());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void Should_ReturnSuccessResponse_When_CallingSuccessWithMessageDataAndStatus() {
        String message = "Operation successful with custom status";
        String data = "Sample data";
        HttpStatus status = HttpStatus.CREATED;

        ResponseEntity<ApiResponse<String>> response = ApiResponseUtil.success(message, data, status);

        assertEquals(status, response.getStatusCode());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(data, response.getBody().getData());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void Should_ReturnErrorResponse_When_CallingErrorWithMessageAndStatus() {
        String message = "Operation failed";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ResponseEntity<ApiResponse<Object>> response = ApiResponseUtil.error(message, status);

        assertEquals(status, response.getStatusCode());
        assertEquals(message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertFalse(response.getBody().isSuccess());
    }
}
