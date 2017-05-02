package it.discovery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.discovery.bootstrap.RestApplication;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@ContextConfiguration(classes=RestApplication.class)
public class BookControllerTest {
	@Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @MockBean
    BookRepository bookRepository;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
        		applicationContext).build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void saveBook() throws Exception {
        Book book = new Book();
        book.setAuthor("author");
        mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(MAPPER.writeValueAsString(book))).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.is(0)));
    }

    @Test
    public void getBook() throws Exception{
        Book book = new Book();
        book.setName("bookname");
        given(bookRepository.findById(1)).willReturn(book);
        mockMvc.perform(get("/book/1")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("bookname")));
    }

    @Test
    public void findBooks() throws Exception {
        given(bookRepository.findAll()).willReturn(Arrays.asList(new Book()));
        mockMvc.perform(get("/book")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(1)));
    }
}

