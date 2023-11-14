package org.emmek.beu2w3d2.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(long id) {
        super("id " + id + " not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}

