package io.rocketbase.lexoffice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventSubscription {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("callbackUrl")
    private String callbackUrl;

}
