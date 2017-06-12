package projectpackage.service.linksservice;

import org.springframework.beans.factory.annotation.Value;
import projectpackage.dto.LinksDTO;

/**
 * Created by Lenovo on 13.06.2017.
 */
public class LinksServiceImpl implements LinksService {

    @Value("${server.http}")
    private String http;
    @Value("${server.https}")
    private String https;

    @Override
    public LinksDTO getLinks() {
        return new LinksDTO(http,https);
    }
}
