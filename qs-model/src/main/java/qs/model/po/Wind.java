package qs.model.po;

import java.util.Date;

public class Wind {
    private Integer windId;

    private String name;

    private Date createTime;

    public Integer getWindId() {
        return windId;
    }

    public void setWindId(Integer windId) {
        this.windId = windId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}