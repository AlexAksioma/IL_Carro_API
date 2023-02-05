package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

public class BookPeriodDto {
    private String email;
    private String startDate;
    private String endDate;


}
