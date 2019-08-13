/*
package qs.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import qs.model.LoginUser;
import qs.util.JsonHelper;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.*;
@Slf4j
public class LoginAuthorizeFilter extends OncePerRequestFilter {
    private AuthenticationManager manager;
    public LoginAuthorizeFilter(AuthenticationManager manager) {
        this.manager = manager;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String cookieName = "sessionlogin";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> cookie = Arrays.asList(cookies).stream()
                    .filter(p -> cookieName.equalsIgnoreCase(p.getName())).findFirst();
            if (cookie.isPresent()) {
                String value = cookie.get().getValue();
                LoginUser v = LoginServiceImpl.decryptLoginWithAutohomeAccount(value);

                if (v != null) {
                    request.setAttribute("_login_user", v);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(v.getId(),
                            "", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    try {
                        Authentication authResult = this.manager.authenticate(authenticationToken);
                        if (authResult != null)
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } catch (Exception ex) {
                        log.warn("身份认证失败", ex.getMessage());
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }


    static class LoginServiceImpl {

        public static LoginUser decryptLoginWithAutohomeAccount(String sessionLogin) {

            return null;
        }

        private final static String KeyForEditAccount = "Z1949A$!CnComAutohomeWww";

        public static String decryptLoginWithEditAccount(String input) {
            try {
                byte[] _a = Base64.getDecoder().decode(input);

                byte[] _b = KeyForEditAccount.getBytes(Charset.forName("UTF-8"));

                byte[] _c = tripleDESDecryptCBC(_b, _b, _a);

                String c = new String(_c);

                return c;
            } catch (Exception e) {

            }
            return null;


        }

        public String encryptLoginWithEditAccount(String input) {
            byte[] _b = KeyForEditAccount.getBytes(Charset.forName("UTF-8"));

            byte[] _d = tripleDESEncryptCBC(_b, _b, input.getBytes(Charset.forName("UTF-8")));

            String d = Base64.getEncoder().encodeToString(_d);

            return null;
        }

        public static byte[] tripleDESDecryptCBC(byte[] key, byte[] keyIV, byte[] data) {

            byte[] result = null;

            try {
                DESedeKeySpec spec = new DESedeKeySpec(key);

                SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

                Key deskey = keyfactory.generateSecret(spec);

                Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");//PKCS5Padding NoPadding

                IvParameterSpec ips = new IvParameterSpec(Arrays.copyOf(keyIV, 8));

                cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

                result = cipher.doFinal(data);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }


            return result;
        }


        public static byte[] tripleDESEncryptCBC(byte[] key, byte[] keyIV, byte[] data) {

            byte[] result = null;

            try {
                DESedeKeySpec spec = new DESedeKeySpec(key);

                SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

                Key deskey = keyfactory.generateSecret(spec);

                Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");

                IvParameterSpec ips = new IvParameterSpec(Arrays.copyOf(keyIV, 8));

                cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

                result = cipher.doFinal(data);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }


            return result;
        }
    }

}
*/
