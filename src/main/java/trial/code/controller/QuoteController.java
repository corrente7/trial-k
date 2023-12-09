package trial.code.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import trial.code.dto.QuoteDto;
import trial.code.model.Quote;
import trial.code.service.QuoteServiceImpl;

import java.util.List;


@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private QuoteServiceImpl quoteService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Quote createQuote(@Valid @RequestBody QuoteDto quoteDto) {
        return quoteService.createQuote(quoteDto);
    }

    @GetMapping(path = "/random")
    public Quote getRandomQuote() {
        return quoteService.getRandomQuote();
    }

    @GetMapping(path = "/{id}")
    public Quote getQuoteById(@PathVariable long id) {
        return quoteService.getQuoteById(id);
    }

    @GetMapping(path = "/top10")
    public List<Quote> getTop10Quotes() {
        return quoteService.getTop10Quotes();
    }

    @GetMapping(path = "/worst10")
    public List<Quote> getWorst10Quotes() {
        return quoteService.getWorst10Quotes();
    }

    @GetMapping(path = "/last3voted")
    public List<Quote> getLast3VotedQuotes() {

        return quoteService.getLast3VotedQuotes();
    }

    @PutMapping(path = "/{id}")
    public Quote updateQuote(@RequestBody QuoteDto quoteDto, @PathVariable long id) {
        return quoteService.updateQuote(quoteDto, id);
    }
    @DeleteMapping(path = "/{id}")
    public void deleteQuote(@PathVariable long id) {
            quoteService.deleteQuote(id);
        }

}
