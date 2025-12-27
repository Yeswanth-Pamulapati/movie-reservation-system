package Service;

import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.service.AuthService;
import com.moviq.movie_reservation_service.service.CustomUserDetails;
import org.mockito.InjectMocks;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.moviq.movie_reservation_service.service.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtTokenService JwtService;
    @InjectMocks
    private AuthService authService;

//    private UserDto userDto;
//    private User user;
//    @BeforeEach
//    void setUp() {
//        userDto = new UserDto();
//        userDto.setId(31);
//        userDto.setEmail("test@example.com");
//        userDto.setPassword("plainPassword");
//        userDto.setRole(Role.CUSTOMER);
//        user = new User();
//        user.setId(31);
//        user.setEmail("test@example.com");
//        user.setPassword("plainPassword");
//        user.setRole(Role.CUSTOMER);
//    }

    @Test
    void loginSuccess(){
        UserDetails userDetails = new User("test@testmail.com","password@123", List.of(new SimpleGrantedAuthority(Role.ADMIN.name())));
//        UsernamePasswordAuthenticationToken AuthToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
//        Authentication authentication = authManager.authenticate(AuthToken);
        when(authManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(JwtService.generateToken("test@testmail.com", List.of()))
                .thenReturn("JWTToken");
        String tokenGenerated = authService.Authenticate("test@testmail.com","Password@123");
        assertEquals("JWTToken",tokenGenerated);
        verify(authManager,times(1)).authenticate(any(Authentication.class));
        verify(JwtService,times(1)).generateToken("test@testmail.com", List.of());


}}
