package Service;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.model.User;
import com.moviq.movie_reservation_service.service.AuthService;
import com.moviq.movie_reservation_service.service.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.moviq.movie_reservation_service.service.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtTokenService JwtService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private AuthService authService;
    private User user;
    private UserDto userDto;
@BeforeEach
   void setUp(){
    // Ensure a clean context before each test
    SecurityContextHolder.clearContext();
        userDto = new UserDto();
        userDto.setId(31);
        userDto.setEmail("test@example.com");
        userDto.setPassword("plainPassword");
        userDto.setRole(Role.CUSTOMER);
        user = new User();
        user.setId(31);
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");
        user.setRole(Role.CUSTOMER);
}


    @Test
    void userProvidedCorrectCredentials_AuthenticationPassed(){
        //Arrange
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole().name().toUpperCase()));
        CustomUserDetails userDetails = new CustomUserDetails(user);
        when(authManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(JwtService.generateToken(user.getEmail(), authorities)).thenReturn("jwt-token");
        //Act
        String tokenGenerated = authService.Authenticate(user.getEmail(),user.getPassword());
        //Assert
        assertEquals("jwt-token",tokenGenerated);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(JwtService,times(1)).generateToken(user.getEmail(), authorities);


}
    @Test
    void userProvidedInCorrectCredentials_AuthenticationFailed(){
        //Arrange
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole().name().toUpperCase()));
        CustomUserDetails userDetails = new CustomUserDetails(user);
        when(authManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthenticationException("Invalid Credentials"){});
        //Act
        assertThrows(AuthenticationException.class,()-> authService.Authenticate(user.getEmail(),"WrongPasswordd!@#"));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        //Assert
        verify(JwtService,never()).generateToken(user.getEmail(), authorities);


    }

    @AfterEach
    void tearDown() {
        // Clean up thread-local to avoid test interference
        SecurityContextHolder.clearContext(); }

}
