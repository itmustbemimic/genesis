package io.neond.genesis.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse extends Throwable{
    private final int errorCode;
    private final String errorMessage;
}
