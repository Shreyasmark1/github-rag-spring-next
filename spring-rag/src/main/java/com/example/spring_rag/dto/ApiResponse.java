package com.example.spring_rag.dto;

enum ResponseStatus {
    success,
    failed,
    error
}

public record ApiResponse<T>(ResponseStatus status, String message, T data) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseStatus.success, "Request completed successfully!", data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(ResponseStatus.success, message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ResponseStatus.success, message, data);
    }

    public static <T> ApiResponse<T> failed(String message) {
        return new ApiResponse<>(ResponseStatus.failed, message, null);
    }

    public static <T> ApiResponse<T> failed(String message, T data) {
        return new ApiResponse<>(ResponseStatus.failed, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ResponseStatus.error, message, null);
    }

}
