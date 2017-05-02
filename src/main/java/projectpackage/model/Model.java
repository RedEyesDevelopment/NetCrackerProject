package projectpackage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Lenovo on 09.04.2017.
 */
@Data
public class Model {
    @JsonProperty("MODEL_ID")
    private int id;
    @JsonProperty("MODEL_DATE")
    private Date dateOfCreation;
    @JsonProperty("MODEL_INNER_OBJECT")
    private InnerObject innerObject;
}
