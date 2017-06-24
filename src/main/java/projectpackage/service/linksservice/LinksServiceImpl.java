package projectpackage.service.linksservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import projectpackage.dto.LinksDTO;

@Service
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
