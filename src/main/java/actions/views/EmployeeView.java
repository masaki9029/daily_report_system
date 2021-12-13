package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeView {

    private Integer id;
    private String code;
    private String name;
    private String password;
    private Integer adminFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleteFlag;

}
