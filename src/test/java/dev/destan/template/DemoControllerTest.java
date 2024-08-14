package dev.destan.template;

import jodd.http.HttpBase;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.HttpStatus;
import jodd.json.JsonObject;
import jodd.json.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created on October, 2022
 *
 * @author destan
 */
@ActiveProfiles("test")
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class DemoControllerTest {

	@LocalServerPort
	private int port;

	@Value("${app.test.valid-jwt}")
	private String validJwt;

	@Value("${app.test.expired-jwt}")
	private String expiredJwt;

	@Test
	@DisplayName("Anonymous user can call public endpoint")
	void anonymousUserPublicPage() {
		//xxx
		final HttpResponse httpResponse = HttpRequest.get(publicPageUrl()).send();

		assertEquals(HttpStatus.HTTP_OK, httpResponse.statusCode());
		assertEquals("anonymousUser", httpResponse.bodyRaw());
	}

	@Test
	@DisplayName("Anonymous user get 401 when call secured page")
	void anonymousUserSecuredPage() {
		final HttpResponse httpResponse = HttpRequest.get(securedPageUrl()).send();

		assertEquals(HttpStatus.HTTP_UNAUTHORIZED, httpResponse.statusCode());
		assertEquals("", httpResponse.bodyRaw());
	}

	@Test
	@DisplayName("Authenticated user can call public page")
	void authenticatedUserPublicPage() {
		final HttpResponse httpResponse = HttpRequest.get(publicPageUrl()).header(HttpBase.HEADER_AUTHORIZATION, "Bearer " + validJwt).send();

		assertEquals(HttpStatus.HTTP_OK, httpResponse.statusCode());

		final JsonObject userJson = new JsonParser().parseAsJsonObject(httpResponse.bodyText());
		assertEquals("john@grr.la", userJson.getString("email"));
		assertEquals(userJson.getJsonArray("roles").getString(0), "ROLE_ADMIN");
	}

	@Test
	@DisplayName("Authenticated user can call secured page")
	void authenticatedUserSecuredPage() {
		final HttpResponse httpResponse = HttpRequest.get(securedPageUrl()).header(HttpBase.HEADER_AUTHORIZATION, "Bearer " + validJwt).send();

		assertEquals(HttpStatus.HTTP_OK, httpResponse.statusCode());

		final JsonObject userJson = new JsonParser().parseAsJsonObject(httpResponse.bodyText());
		assertEquals("john@grr.la", userJson.getString("email"));
		assertEquals(userJson.getJsonArray("roles").getString(0), "ROLE_ADMIN");
	}

	@Test
	@DisplayName("User with expired token get 401 when call public page")
	void userWithExpiredTokenPublicPage() {
		final HttpResponse httpResponse = HttpRequest.get(publicPageUrl()).header(HttpBase.HEADER_AUTHORIZATION, "Bearer " + expiredJwt).send();

		assertEquals(HttpStatus.HTTP_UNAUTHORIZED, httpResponse.statusCode());
		assertEquals("", httpResponse.bodyRaw());
	}

	@Test
	@DisplayName("User with expired token get 401 when call secured page")
	void userWithExpiredTokenSecuredPage() {
		final HttpResponse httpResponse = HttpRequest.get(securedPageUrl()).header(HttpBase.HEADER_AUTHORIZATION, "Bearer " + expiredJwt).send();

		assertEquals(HttpStatus.HTTP_UNAUTHORIZED, httpResponse.statusCode());
		assertEquals("", httpResponse.bodyRaw());
	}

	private String publicPageUrl() {
		return "http://localhost:" + port + "/demo";
	}

	private String securedPageUrl() {
		return "http://localhost:" + port + "/demo/secured";
	}
}