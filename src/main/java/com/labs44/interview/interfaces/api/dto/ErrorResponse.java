package com.labs44.interview.interfaces.api.dto;


public record ErrorResponse(
        boolean isSuccess,
        String code,
        String message
) {
}
