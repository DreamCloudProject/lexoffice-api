package io.rocketbase.lexoffice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Currency {

    EUR("EUR");

    @Getter
    @JsonValue
    private String value;

    Currency(String value) {
        this.value = value;
    }

}
