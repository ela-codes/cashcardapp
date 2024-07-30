package example.cashcard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// starts the spring boot application so our tests can perform requests to it
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashCardApplicationTests {
    @Autowired
    // inject "TestRestTemplate" test helper that’ll allow us to make HTTP requests to the locally running application
    TestRestTemplate restTemplate;

	@Test
	void shouldReturnACashCardWhenDataIsSaved() {
        // this is a GET request to our application endpoint /cashcards/99
        // restTemplate returns a ResponseEntity which provides info about what happened to the request
        ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/99", String.class);
        //<String> specifies that the ResponseEntity instance will contain a response body of type String.

        // ResponseEntity in the Spring Framework used to represent an HTTP response, including the status code, headers, and body.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
	}

}
