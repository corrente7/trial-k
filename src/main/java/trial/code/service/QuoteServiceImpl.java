package trial.code.service;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import trial.code.dto.QuoteDto;
import trial.code.model.Quote;
import trial.code.model.Vote;
import trial.code.repository.QuoteRepository;
import trial.code.repository.VoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
@AllArgsConstructor
public class QuoteServiceImpl {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private VoteRepository voteRepository;


    public Quote createQuote(QuoteDto quoteDto) {
        Quote quote = new Quote();
        quote.setContent(quoteDto.getContent());
        return quoteRepository.save(quote);
    }

    public Quote getRandomQuote() {
        long qty = quoteRepository.count();
        int idx = (int) (Math.random() * qty);
        Pageable pageRequest = PageRequest.of(idx, 1);
        Page<Quote> quotePage = quoteRepository.findAll(pageRequest);
        Quote quote = null;
        if (quotePage.hasContent()) {
            quote = quotePage.getContent().get(0);
        }
        return quote;
    }

    public Quote getQuoteById(long id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Quote not found"));
    }

    public List<Quote> getTop10Quotes() {

        return quoteRepository.findTop10ByOrderByScoreDesc();
    }

    public List<Quote> getWorst10Quotes() {
        return quoteRepository.findTop10ByOrderByScoreAsc();
    }

    public List<Quote> getLast3VotedQuotes() {

        List<Vote> votes = voteRepository.findFirst3ByOrderByCreatedAtDesc();
        List<Quote> last3Quotes = new ArrayList<>();
        for (Vote vote : votes) {
            last3Quotes.add(vote.getQuote());
        }

        return last3Quotes;
    }

    public Quote updateQuote(QuoteDto quoteDto, @PathVariable long id) {
        if (!quoteRepository.existsById(id)) {
            throw new NoSuchElementException("Quote not found");
        }
        Quote quote = quoteRepository.findById(id).orElseThrow();

        quote.setContent(quoteDto.getContent());
        return quoteRepository.save(quote);
    }

    public void deleteQuote(long id) {
        if (!quoteRepository.existsById(id)) {
            throw new NoSuchElementException("Quote not found");
        }
        quoteRepository.deleteById(id);
    }
}
