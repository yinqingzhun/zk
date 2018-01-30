package qs.service.implement;

import org.springframework.stereotype.Service;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;
import qs.service.HelloService;

@DbChoosing(EnumDataSourceName.TICKET_BASE)
@Service
public class HelloServiceImpl implements HelloService {
    //@DbChoosing(EnumDataSourceName.TICKET_USER)
    @Override
    public void hello() {
        String word = getWord();
        System.out.println(word);
    }

    //@DbChoosing(EnumDataSourceName.TICKET_ORDER)
    private String getWord() {
        return "hello,boddy!";
    }

    //@DbChoosing(EnumDataSourceName.TICKET_ORDER)
    private void sayHi() {

    }
}
