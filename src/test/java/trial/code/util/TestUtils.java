package trial.code.util;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import trial.code.model.Quote;
import trial.code.model.User;
import trial.code.model.Vote;
import trial.code.repository.QuoteRepository;
import trial.code.repository.UserRepository;
import trial.code.repository.VoteRepository;


@Component
public class TestUtils {

    private Model<User> userModel;

    private Model<Quote> quoteModel;

    private Model<Vote> voteModel;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private VoteRepository voteRepository;

    @PostConstruct
    private void init() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();

        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getUserId))
                .set(Select.field(User::getEmail), email)
                .toModel();

        quoteModel = Instancio.of(Quote.class)
                .ignore(Select.field(Quote::getQuoteId))
                .toModel();

        voteModel = Instancio.of(Vote.class)
                .ignore(Select.field(Vote::getVoteId))
                .toModel();


    }
    public void clean() {
        quoteRepository.deleteAll();
        voteRepository.deleteAll();
        userRepository.deleteAll();
    }

    public Model<User> getUserModel() {
        return userModel;
    }

    public Model<Quote> getQuoteModel() {
        return quoteModel;
    }

    public Model<Vote> getVoteModel() {
        return voteModel;
    }
}
