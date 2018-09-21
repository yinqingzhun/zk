package qs.service.implement;

import org.springframework.stereotype.Service;
import qs.config.db.DbChoosing;
import qs.config.db.EnumDataSourceName;
import qs.service.HelloService;

@DbChoosing(EnumDataSourceName.USER)
@Service
public class HelloServiceImpl implements HelloService {
    //@DbChoosing(EnumDataSourceName.TICKET_USER)
    @Override
    public void hello() {
        String word = getWord();
        System.out.println(word);
    }

    //@DbChoosing(EnumDataSourceName.TOPIC)
    private String getWord() {
        return "hello,boddy!";
    }

    //@DbChoosing(EnumDataSourceName.TOPIC)
    private void sayHi() {

    }
}
