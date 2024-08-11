package dev.destan.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created on May, 2022
 *
 * @author destan
 */
@Slf4j
@RestController
@Profile({"test", "dev"})
@RequestMapping("demo")
class DemoController {

	@GetMapping
	ResponseEntity<?> demo(@AuthenticationPrincipal Object user, Principal principal) {
		log.info("demo user: {}", user);
		// When not authenticated user: anonymousUser (String), principle: null
		return ResponseEntity.ok(user);
	}

	@GetMapping("secured")
	ResponseEntity<?> demoSecured(@AuthenticationPrincipal Object user, Principal principal) {
		log.info("SECURED demo user: {}", user);
		return ResponseEntity.ok(user);
	}

}
