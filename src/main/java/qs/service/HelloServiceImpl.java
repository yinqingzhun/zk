package qs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public void hello() {
        System.out.println(getWord());
    }

    @DbChoosing(EnumDataSourceName.TICKET_BASE)
    private String getWord() {
        return "hello,boddy!";
    }
}
