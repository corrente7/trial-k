package trial.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import trial.code.dto.UserDto;
import trial.code.repository.UserRepository;
import trial.code.util.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UsersTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestUtils testUtils;

    @AfterEach
    void clean() {
        testUtils.clean();
    }

    @Test
    public void createUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Mark");
        userDto.setEmail("mark@google.com");
        userDto.setPassword("12345");

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userDto)))

                .andReturn()
                .getResponse();


        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(userRepository.findByName("Mark").get().toString()).contains("mark@google.com");

    }
}
