package dev.destan.template.announcement;

import java.time.LocalDateTime;

public record Announcement(String title, String content, LocalDateTime date) {
}
