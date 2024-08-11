package dev.destan.template;

import java.time.ZonedDateTime;

public interface SoftDeletable {
    ZonedDateTime deletedAt();

    Long deletedBy();
}
