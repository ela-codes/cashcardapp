package example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cashCardRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                //default paging is page=0&size=20
                //defined values for sorting
            ));
        return ResponseEntity.ok(page.getContent());
    }
}

