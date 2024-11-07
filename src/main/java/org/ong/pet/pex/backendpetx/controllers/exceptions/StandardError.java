package org.ong.pet.pex.backendpetx.controllers.exceptions;

import lombok.*;

import java.io.*;
import java.time.Instant;

@Getter
@Setter
public class StandardError implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}
