package qs.persist;

import qs.model.po.Wind;

public interface WindMapper {
    int deleteByPrimaryKey(Integer windId);

    int insert(Wind record);

    int insertSelective(Wind record);

    Wind selectByPrimaryKey(Integer windId);

    int updateByPrimaryKeySelective(Wind record);

    int updateByPrimaryKey(Wind record);
}