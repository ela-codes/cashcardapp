package example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * The @JsonTest annotation marks the CashCardJsonTest as a test class which uses the Jackson framework (which is * included as part of Spring). This provides extensive JSON testing and parsing support. It also establishes all * the related behavior to test JSON objects.
 * JacksonTester is a convenience wrapper to the Jackson JSON parsing library. It handles serialization and 
 * deserialization of JSON objects.
 * @Autowired is an annotation that directs Spring to create an object of the requested type.
 */
@JsonTest
class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                    new CashCard(99L, 123.45),
                    new CashCard(100L, 1.00),
                    new CashCard(101L, 150.00));
    }

    /*
     * Runs the test for converting an object into a string.
     * TDD - Design this test so that it is the expected outcome. 
     * (ie. include the necessary classes like instantiating things. it will fail but the next step is to write the code so that it passes the test.)
     */
    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = cashCards[0];
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
        // this json file is the data contract which determines the structure, format, and semantics of the data being exchanged
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 99,
                    "amount": 123.45
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected = """
                [
                    {"id": 99, "amount": 123.45},
                    {"id":100, "amount": 1.00},
                    {"id": 101, "amount": 150.00}
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
} 
