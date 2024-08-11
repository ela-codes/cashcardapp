package example.cashcard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {
    // the method name follows a naming convention that Spring Data JDBC recognizes
    // it parses the name in order to create an SQL query
    
    CashCard findByIdAndOwner(Long id, String owner);
    Page<CashCard> findByOwner(String owner, PageRequest pageRequest);
    boolean existsByIdAndOwner(Long id, String owner);

}
