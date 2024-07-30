package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //annotates this class as capable of handling http requests
@RequestMapping("/cashcards") // companion to @RestController that indicates which address requests must have in order to access this controller
class CashCardController {

    @GetMapping("/{requestedId}") //marks findById() as a handler method for GET requests that match cashcards/{requestedId}
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        if(requestedId.equals(99L)) {
            CashCard cashCard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(cashCard);
        }else {
            return ResponseEntity.notFound().build();
        }
        
    }
}

