package tests.restandsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import projectpackage.Application;
import projectpackage.dto.UserPasswordDTO;
import tests.AbstractTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Lenovo on 21.05.2017.
 */
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ContextConfiguration(classes= Application.class)
public class SecurityTests extends AbstractTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                applicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", authorities = {"ADMIN"})
    public void getUser() throws Exception {
        int userId=900;
        mockMvc.perform(get("/users/900")).andExpect(status().is(202)).andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("stephenking@mail.ru")));
    }

    @Test
    @WithMockUser(username = "alekseenko365@gmail.com", authorities = {"ADMIN"})
    public void changeUserPassword() throws Exception {
        int userId=2060;
        UserPasswordDTO dto = new UserPasswordDTO();
        dto.setOldPassword("qwerty");
        dto.setNewPassword("asdfgh");
        mockMvc.perform(put("/users/update/password/900",dto)).andExpect(status().is(200)).andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("stephenking@mail.ru")));
    }
}
