package trial.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import trial.code.dto.VoteDto;
import trial.code.model.Quote;
import trial.code.model.User;
import trial.code.repository.QuoteRepository;
import trial.code.repository.UserRepository;
import trial.code.util.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static trial.code.model.VoteType.DOWNVOTE;
import static trial.code.model.VoteType.UPVOTE;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VotesTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtils;

    private Quote testQuote;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(testUtils.getUserModel())
                .create();
        userRepository.save(testUser);
        testQuote = Instancio.of(testUtils.getQuoteModel())
                .set(Select.field(Quote::getAuthor), testUser)
                .create();
        quoteRepository.save(testQuote);
    }

    @Test
    public void voteUpTest() throws Exception {

        testQuote.setScore(90);
        quoteRepository.save(testQuote);

        VoteDto voteUp = new VoteDto(UPVOTE, testQuote.getQuoteId());

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(voteUp)))

                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        Long id = testQuote.getQuoteId();

        MockHttpServletResponse response1 = mockMvc
                .perform(get("/api/quotes/" + id))
                .andReturn().getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentAsString()).contains("91");
    }

    @Test
    public void voteDownTest() throws Exception {

        testQuote.setScore(120);
        quoteRepository.save(testQuote);

        VoteDto voteUp = new VoteDto(DOWNVOTE, testQuote.getQuoteId());

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(voteUp)))

                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        Long id = testQuote.getQuoteId();

        MockHttpServletResponse response1 = mockMvc
                .perform(get("/api/quotes/" + id))
                .andReturn().getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentAsString()).contains("119");
    }
}
