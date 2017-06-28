package projectpackage.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by Arizel on 28.06.2017.
 */
@Data
public class BlockDTO {
    private Date blockStartDate;
    private Date blockFinishDate;
    private String reason;
    private Integer roomId;
}
