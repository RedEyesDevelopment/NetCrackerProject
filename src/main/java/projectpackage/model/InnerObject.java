package projectpackage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Lenovo on 09.04.2017.
 */
@Data
public class InnerObject {
    @JsonProperty("INNER_ID")
    private int innerId;
    @JsonProperty("INNER_DATA")
    private String data;
}
