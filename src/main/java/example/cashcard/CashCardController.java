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
import java.security.Principal;

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
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
        Optional<CashCard> cashCardOptional = Optional.ofNullable(
            cashCardRepository.findByIdAndOwner(
                requestedId, principal.getName()
            )
        );
        
        if(cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping // annotation for POST endpoint
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal) {
        CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());

        // crudRepository built in method saves the new CashCard and returns a unique ID
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);

        // build a URI that will get returned as part of the POST response
    
        URI locationOfNewCashCard = ucb
                                    .path("cashcards/{id}")
                                    .buildAndExpand(savedCashCard.id())
                                    .toUri();

        // return a ResponseEntity that should have 201 Created and the Location header set to the URI that was built
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                //default paging is page=0&size=20
                //defined values for sorting
            ));
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
        CashCard cashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        // application should return 404 not found if cashCard could not be found (otherwise program "crashes" and returns 403 forbidden, courtesy of spring security)
        if(cashCard != null) {
            CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
            cashCardRepository.save(updatedCashCard);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}

