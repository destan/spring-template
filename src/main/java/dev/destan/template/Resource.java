package dev.destan.template;

import java.time.ZonedDateTime;

public interface Resource {

    Long id();

    ZonedDateTime createdAt();

    Long createdBy();

    ZonedDateTime updatedAt();

    Long updatedBy();

    Integer version();

}
