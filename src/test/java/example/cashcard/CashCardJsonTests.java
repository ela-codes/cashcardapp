package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * The @JsonTest annotation marks the CashCardJsonTest as a test class which uses the Jackson framework (which is included as part of Spring). This provides extensive JSON testing and parsing support. It also establishes all the related behavior to test JSON objects.

JacksonTester is a convenience wrapper to the Jackson JSON parsing library. It handles serialization and deserialization of JSON objects.
@Autowired is an annotation that directs Spring to create an object of the requested type.
 */


@JsonTest
class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    /**
     * Runs the test for converting an object into a string.
     * TDD - Design this test so that it is the expected outcome. 
     * (ie. include the necessary classes like instantiating things. it will fail but the next step is to write the code so that it passes the test.)
     */
    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45);
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");
        // this json file is the data contract which determines the structure, format, and semantics of the data being exchanged
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
    }
} 
