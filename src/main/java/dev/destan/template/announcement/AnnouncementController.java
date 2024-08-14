package dev.destan.template.announcement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created on Agu, 2024
 *
 * @author destan
 */
@Slf4j
@RestController
@RequestMapping("public/announcements")
class AnnouncementController {

    @GetMapping
    List<Announcement> list() {
        return List.of(new Announcement("First one", "Lorem", LocalDateTime.now()));
    }

}
