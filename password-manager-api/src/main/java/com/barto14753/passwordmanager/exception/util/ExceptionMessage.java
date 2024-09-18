package com.barto14753.passwordmanager.exception.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExceptionMessage {
    private String logMessage;
    private String message;
}
