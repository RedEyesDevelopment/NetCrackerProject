package projectpackage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BlockDTO {
    private Date blockStartDate;
    private Date blockFinishDate;
    private String reason;
    private Integer roomId;
}
