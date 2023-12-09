package trial.code.service;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trial.code.dto.VoteDto;
import trial.code.model.Quote;
import trial.code.model.Vote;
import trial.code.repository.QuoteRepository;
import trial.code.repository.VoteRepository;

import java.util.NoSuchElementException;

import static trial.code.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteServiceImpl {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Transactional
    public void vote(VoteDto voteDto) {
        Quote quote = quoteRepository.findById(voteDto.getQuoteId())
                .orElseThrow(() -> new NoSuchElementException("Quote Not Found"));

        if (UPVOTE.equals(voteDto.getVoteType())) {
            quote.setScore(quote.getScore() + 1);
        } else {
            quote.setScore(quote.getScore() - 1);
        }

        Vote vote = new Vote();
        vote.setVoteType(voteDto.getVoteType());
        vote.setQuote(quote);

        quoteRepository.save(quote);
        voteRepository.save(vote);

    }


}
