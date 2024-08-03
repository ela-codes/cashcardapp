package example.cashcard;

import org.springframework.data.annotation.Id;

record CashCard(@Id long id, double amount) {
    
}