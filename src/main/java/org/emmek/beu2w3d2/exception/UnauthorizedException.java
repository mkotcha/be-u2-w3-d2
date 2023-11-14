package org.emmek.beu2w3d2.exception;


import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}