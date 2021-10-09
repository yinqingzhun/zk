package qs.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qs.model.User;
import reactor.core.publisher.Mono;

import java.net.InetAddress;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
@Slf4j
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeController {


    @ResponseBody
    @PostMapping("")
    public Object index(User user) {
        return Mono.justOrEmpty(user);
    }

    private void run() {
        Agent agent = new Agent(); // A simple ICE Agent

/*** Setup the STUN servers: ***/
        String[] hostnames = new String[]{"jitsi.org", "numb.viagenie.ca", "stun.ekiga.net"};
// Look online for actively working public STUN Servers. You can find free servers.
// Now add these URLS as Stun Servers with standard 3478 port for STUN servrs.
        for (String hostname : hostnames) {
            try {
                // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
                TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);
                // Currently Ice4J only supports UDP and will throw an Error otherwise
                agent.addCandidateHarvester(new StunCandidateHarvester(ta));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IceMediaStream stream = agent.createMediaStream("audio");
        int port = 5000; // Choose any port
//        agent.createComponent(stream, Transport.UDP, port, port, port+100);
// The three last arguments are: preferredPort, minPort, maxPort



    }

    @ResponseBody
    @PostMapping("user")
    public Mono<User> user(User user) {
        return Mono.justOrEmpty(user);
    }

    @ResponseBody
    @GetMapping("any")
    public Mono<User> any() {
        log.warn("try something new!!", new NullPointerException());
        return Mono.justOrEmpty(new User() {
            {
                setId("id-1388");
                setUsername("Jack");
                setEmail("jack@qq.com");
            }
        });
    }


}
