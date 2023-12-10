package trial.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import trial.code.dto.QuoteDto;
import trial.code.model.Quote;
import trial.code.model.User;
import trial.code.repository.QuoteRepository;
import trial.code.repository.UserRepository;
import trial.code.util.TestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class QuotesTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

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
                .set(Select.field(Quote::getScore), 1000)
                .set(Select.field(Quote::getContent), "content")
                .create();
        quoteRepository.save(testQuote);
    }

    @AfterEach
    void clean() {
        testUtils.clean();
    }

    @Test
    public void getQuotesTest() throws Exception {

        List<Quote> list = new ArrayList<>();

        for (int i = 777; i < 788; i++) {
            list.add(Instancio.of(testUtils.getQuoteModel())
                    .set(Select.field(Quote::getAuthor), testUser)
                    .set(Select.field(Quote::getScore), i)
                    .create()
            );
        }
        quoteRepository.saveAll(list);

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/quotes/top10"))
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("1000");
        assertThat(response.getContentAsString()).doesNotContain("777");

        MockHttpServletResponse response1 = mockMvc
                .perform(get("/api/quotes/worst10"))
                .andReturn()
                .getResponse();

        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getContentAsString()).contains("777");
        assertThat(response1.getContentAsString()).doesNotContain("1000");
    }

    @Test
    public void getQuoteTest() throws Exception {

        Long id = testQuote.getQuoteId();

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/quotes/" + id))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(testQuote.getContent());
        assertThat(response.getContentAsString()).contains(testQuote.getAuthor().getName());
    }

    @Test
    public void getRandomQuoteTest() throws Exception {

        Quote testQuote1 = Instancio.of(testUtils.getQuoteModel())
                .set(Select.field(Quote::getAuthor), testUser)
                .set(Select.field(Quote::getContent), "content1")
                .create();

        quoteRepository.save(testQuote1);

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/quotes/random"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).containsPattern(".*(content1|content).*");
    }

    @Test
    public void createQuoteTest() throws Exception {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setContent("rrr");

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(quoteDto)))

                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        MockHttpServletResponse response1 = mockMvc
                .perform(get("/api/quotes/worst10"))
                .andReturn()
                .getResponse();

        assertThat(response1.getContentAsString()).contains("rrr");
    }

    @Test
    public void updateQuoteTest() throws Exception {

        Long id = testQuote.getQuoteId();
            QuoteDto quoteDto = new QuoteDto("77777");

        MockHttpServletResponse response = mockMvc
                .perform(put("/api/quotes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(quoteDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        MockHttpServletResponse response1 = mockMvc
                .perform(get("/api/quotes/" + id))
                .andReturn().getResponse();

        assertThat(response1.getContentAsString()).contains("77777");
    }
    @Test
    public void deleteQuoteTest() throws Exception {

        Long id = testQuote.getQuoteId();

        MockHttpServletResponse response = mockMvc
                .perform(delete("/api/quotes/" + id))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(quoteRepository.findById(id).isEmpty());
    }
}
