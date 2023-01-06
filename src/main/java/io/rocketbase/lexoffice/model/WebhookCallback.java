package io.rocketbase.lexoffice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebhookCallback {

    @JsonProperty("organizationId")
    private String organizationId;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("resourceId")
    private String resourceId;

    @JsonProperty("eventDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date eventDate;

}
