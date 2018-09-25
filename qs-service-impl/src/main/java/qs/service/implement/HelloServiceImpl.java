package qs.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;
import qs.model.Suser;
import qs.service.HelloService;

import java.util.List;
@Slf4j
@DbChoosing(EnumDataSourceName.TOPIC)

@Service
public class HelloServiceImpl implements HelloService {
    //@DbChoosing(EnumDataSourceName.TICKET_USER)
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Object hello() {

        RowMapper<Suser> rm = BeanPropertyRowMapper.newInstance(Suser .class);
      List<Suser> list = jdbcTemplate.query("select * from suser limit 10",new Object[0], rm);
      return list;
    }

    //@DbChoosing(EnumDataSourceName.TOPIC)
    private String getWord() {
        return "hello,boddy!";
    }

    //@DbChoosing(EnumDataSourceName.TOPIC)
    private void sayHi() {

    }
}
