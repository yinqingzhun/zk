package qs.model.po;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
@Data
@RequiredArgsConstructor(staticName = "showMe")
public class Wind {
    private final    Integer windId;

    private String name;

    private Date createTime;


}