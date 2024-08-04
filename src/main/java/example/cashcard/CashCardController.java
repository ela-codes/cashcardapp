package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import java.net.URI;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

@RestController //annotates this class as capable of handling http requests
@RequestMapping("/cashcards") // companion to @RestController that indicates which address requests must have in order to access this controller
class CashCardController {

    // inject the repository. this will manage the CashCard data instead of the Controller
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}") 
    //marks findById() as a handler method for GET requests that match cashcards/{requestedId}
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        
        if(cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping // annotation for POST endpoint
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb) {
        // crudRepository built in method saves the new CashCard and returns a unique ID
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);

        // build a URI that will get returned as part of the POST response
    
        URI locationOfNewCashCard = ucb
                                    .path("cashcards/{id}")
                                    .buildAndExpand(savedCashCard.id())
                                    .toUri();

        // return a ResponseEntity that should have 201 Created and the Location header set to the URI that was built
        return ResponseEntity.created(locationOfNewCashCard).build();
    }
}

