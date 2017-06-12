package projectpackage.dto;

import lombok.Data;

@Data
public class LinksDTO {
    public String http;
    public String https;

    public LinksDTO(String http, String https) {
        this.http = http;
        this.https = https;
    }
}
