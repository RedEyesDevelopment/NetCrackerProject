package it.discovery.client;

import it.discovery.model.Book;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Lenovo on 23.04.2017.
 */
public class RestClient {
    private String username;
    private String password;
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        RestClient restClient = new RestClient("admin","admin");
//        Book book = restClient.getBook();
//        if (null==book) {
//            System.out.println("Book is null");
//        } else System.out.println(book.toString());

        System.out.println(restClient.saveBook());
    }

    public RestClient(String username, String password) {
        this.username=username;
        this.password=password;
    }

    public Book getBook(){
        return restTemplate.getForObject("http://localhost:8080/book/2",Book.class,username,password);
    }

    public int saveBook(){
        return restTemplate.postForObject("http://localhost:8080/book", new Book(),Integer.class,username,password);
    }
}
