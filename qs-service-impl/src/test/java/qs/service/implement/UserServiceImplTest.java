package qs.service.implement;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import qs.model.JwtUser;
import qs.repository.UserRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserServiceImpl userServiceImplUnderTest;

    @Before
    public void setUp() {
        userServiceImplUnderTest = new UserServiceImpl();
        userServiceImplUnderTest.userReporitory = mock(UserRepository.class);
    }

    @Test
    public void testLoadUserByUsername() {
        // Setup
        // Configure UserRepository.getUserByUserName(...).
        final JwtUser jwtUser = new JwtUser("id", "username", "password", "email", Arrays.asList(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(userServiceImplUnderTest.userReporitory.getUserByUserName("username")).thenReturn(jwtUser);

        // Run the test
        final UserDetails result = userServiceImplUnderTest.loadUserByUsername("username");

        // Verify the results
        assertThat(result).isEqualTo(jwtUser);
    }

    @Test
    public void testLoadUserByUsername_UserRepositoryReturnsNull() {
        // Setup
        when(userServiceImplUnderTest.userReporitory.getUserByUserName("username")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void testLoadUserByUsername_ThrowsUsernameNotFoundException() {
        // Setup
        // Configure UserRepository.getUserByUserName(...).

        when(userServiceImplUnderTest.userReporitory.getUserByUserName("username")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
