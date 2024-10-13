package com.nordnet.order_manager.utils;

import com.nordnet.order_manager.models.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseUtilTest {

    @Test
    void Should_ReturnCorrectSuccessResponse_When_CallingSuccessWithMessageAndData() {

        // Arrange
        String message = "Operation successful";
        String data = "Sample data";
        ApiResponse<String> expectedResponseBody = new ApiResponse<String>()
                .setMessage(message)
                .setData(data)
                .setSuccess(true);

        // Act
        ResponseEntity<ApiResponse<String>> response = ApiResponseUtil.success(message, data);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void Should_ReturnCorrectSuccessResponse_When_CallingSuccessWithCustomStatus() {
        // Arrange
        String message = "Operation successful with custom status";
        String data = "Sample data";
        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<String> expectedResponseBody = new ApiResponse<String>()
                .setMessage(message)
                .setData(data)
                .setSuccess(true);

        // Act
        ResponseEntity<ApiResponse<String>> response = ApiResponseUtil.success(message, data, status);

        // Assert
        assertEquals(status, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void Should_ReturnErrorResponse_When_CallingErrorWithMessageAndStatus() {
        // Arrange
        String message = "Operation failed";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<String> expectedResponseBody = new ApiResponse<String>()
                .setSuccess(false)
                .setMessage(message)
                .setData(null);

        // Act
        ResponseEntity<ApiResponse<String>> response = ApiResponseUtil.error(message, status);

        // Assert
        assertEquals(status, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }
}
