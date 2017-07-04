package projectpackage.dto;

import lombok.Data;

@Data
public class ConfirmPaidOrderDTO {
    private Boolean isPaidFor;
    private Boolean isConfirmed;
}
