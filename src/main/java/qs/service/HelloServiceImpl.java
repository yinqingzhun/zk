package qs.service;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello() {
        System.out.println(getWord());
    }
    @Override
    public String getWord() {
        return "hello,boddy!";
    }
}
