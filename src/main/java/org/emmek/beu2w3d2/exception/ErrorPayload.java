package org.emmek.beu2w3d2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class ErrorPayload {
    private String message;
    private Date timestamp;

}