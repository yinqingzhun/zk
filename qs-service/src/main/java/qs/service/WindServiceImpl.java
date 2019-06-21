package qs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;
import qs.model.po.Wind;
import qs.persist.WindMapper;

@DbChoosing(value = EnumDataSourceName.WINDS)
@Component
public class WindServiceImpl implements WindService {
    @Autowired
    WindMapper windMapper;

    @Override
    public Wind get(int id) {
        return windMapper.selectByPrimaryKey(id);
    }
}
