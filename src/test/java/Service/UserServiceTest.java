package Service;

import com.moviq.movie_reservation_service.dto.UserDto;
import com.moviq.movie_reservation_service.exception.UserNotFoundException;
import com.moviq.movie_reservation_service.exception.UsernameAlreadyExistsException;
import com.moviq.movie_reservation_service.mapper.UserMapper;
import com.moviq.movie_reservation_service.model.Role;
import com.moviq.movie_reservation_service.model.User;
import com.moviq.movie_reservation_service.model.UserStatus;
import com.moviq.movie_reservation_service.repository.UserRepo;
import com.moviq.movie_reservation_service.service.UserService;
import jakarta.validation.Valid;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
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
    void FindUserById() throws UserNotFoundException {
        //Arrange
        when(userRepo.findById(31)).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        //Act & Assert
        UserDto userDto = userService.findUserById(31);
        assertEquals(31, userDto.getId());
        verify(userRepo, times(1)).findById(31);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void userNotFoundById() {
        //Arrange
        when(userRepo.findById(31)).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(31));
        verify(userRepo, times(1)).findById(31);
        verify(userMapper, never()).toDto(user);
    }

    @Test
    void emailAlreadyExists() {
        //Arrange
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode(eq("plainPassword"))).thenReturn("encodedPassword");
        when(userRepo.existsByEmail(eq("test@example.com"))).thenReturn(true);
        //Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.registerUser(userDto));
        verify(userRepo, times(1)).existsByEmail("test@example.com");
        verify(userRepo, never()).save(any(User.class));

    }

    @Test
    void userRegistrationSuccess() {
        //Arrange
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode(eq("plainPassword"))).thenReturn("encodedPassword");
        when(userRepo.existsByEmail(eq("test@example.com"))).thenReturn(false);
        //Act & Assert
        String registrationResult = userService.registerUser(userDto);
        assertEquals("User Registered Successfully", registrationResult);
        verify(userRepo, times(1)).existsByEmail("test@example.com");
        verify(userRepo, times(1)).save(any(User.class));

    }

    @Test
    void findUserByUsername() throws UserNotFoundException {
        //Arrange
        when(userRepo.findByEmail(eq("test@example.com"))).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        //Act
        UserDto userdto = userService.findUserByUsername("test@example.com");
        assertNotNull(userdto);
        assertEquals("test@example.com", userdto.getEmail());
        verify(userRepo, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).toDto(any(User.class));

    }

    @Test
    void userNotFoundByUsername() throws UserNotFoundException {
        //Arrange
        when(userRepo.findByEmail(eq("test@example.com"))).thenReturn(Optional.empty());
        //Act
        assertThrows(UserNotFoundException.class, () -> userService.findUserByUsername("test@example.com"));
        verify(userRepo, times(1)).findByEmail("test@example.com");
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    void usersFoundBySpecifiedRole() throws UserNotFoundException {
        //Arrange
        when(userRepo.findByRole(any(Role.class))).thenReturn(Collections.singletonList(user));
        when(userMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(userDto));
        //Act
        List<UserDto> usersDto = userService.findUsersByRole(Role.CUSTOMER.name());
        assertNotNull(usersDto);
        verify(userRepo, times(1)).findByRole(any(Role.class));
        verify(userMapper, times(1)).toDtoList(anyList());

    }

    @Test
    void noUserFoundBySpecifiedRole() throws UserNotFoundException {
        //Arrange
        when(userRepo.findByRole(any(Role.class))).thenReturn(List.of());
        //Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findUsersByRole(Role.CUSTOMER.name()));
        verify(userRepo, times(1)).findByRole(any(Role.class));
        verify(userMapper, never()).toDtoList(anyList());

    }

    @Test
    void invalidRoleProvided() throws IllegalArgumentException {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.findUsersByRole("Developer"));
        verify(userRepo, never()).findByRole(any(Role.class));
        verify(userMapper, never()).toDtoList(anyList());

    }

    @Test
    void getUsersByRegisterdDate() {
        when(userRepo.findByCreatedAtBetween(eq(LocalDate.now().atStartOfDay()), eq(LocalDate.now().atTime(LocalTime.MAX)))).thenReturn(Collections.singletonList(user));
        when(userMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(userDto));
        // Act
        List<UserDto> users = userService.findUserByRegisteredDate(LocalDate.now()); // Assert
        assertTrue(!users.isEmpty());
    }

    @Test
    void noUsersFoundByRegisterdDate() {
        when(userRepo.findByCreatedAtBetween(eq(LocalDate.now().atStartOfDay()), eq(LocalDate.now().atTime(LocalTime.MAX)))).thenReturn(List.of());
        when(userMapper.toDtoList(anyList())).thenReturn(List.of());
        // Act
        List<UserDto> users = userService.findUserByRegisteredDate(LocalDate.now()); // Assert
        assertTrue(users.isEmpty());
    }


    @Test
    void usersFoundBySpecifiedStatus() throws UserNotFoundException {
        //Arrange
        when(userRepo.findByStatus(anyString())).thenReturn(Collections.singletonList(user));
        when(userMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(userDto));
        //Act
        List<UserDto> usersDto = userService.findUsersByStatus("ACTIVE");
        assertNotNull(usersDto);
        verify(userRepo, times(1)).findByStatus(anyString());
        verify(userMapper, times(1)).toDtoList(anyList());

    }

    @Test
    void noUserFoundBySpecifiedStatus() throws UserNotFoundException {
        //Arrange
        when(userRepo.findByStatus(anyString())).thenReturn(List.of());
        //Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findUsersByStatus("ACTIVE"));
        verify(userRepo, times(1)).findByStatus(anyString());
        verify(userMapper, never()).toDtoList(anyList());

    }

    @Test
    void invalidStatusProvided() throws IllegalArgumentException {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.findUsersByStatus("DORMANT"));
        verify(userRepo, never()).findByStatus(anyString());
        verify(userMapper, never()).toDtoList(anyList());

    }

    @Test
    void noUserFoundToUpdateTheRole() throws UserNotFoundException {
        when(userRepo.existsById(31L)).thenReturn(false);
        assertThrows(UserNotFoundException.class,()->userService.updateUserRole(31L,Role.ADMIN));
        verify(userRepo,never()).updateUserRole(31L,Role.ADMIN);
        verify(userRepo,never()).save(user);
    }
    @Test
    void updateUserRole() throws UserNotFoundException {
        when(userRepo.existsById(31L)).thenReturn(true);
        when(userRepo.findById(31L)).thenReturn(Optional.ofNullable(user));
        when(userRepo.updateUserRole(31L,Role.ADMIN)).thenReturn(1);
        int rowsUpdated= userService.updateUserRole(31L,Role.ADMIN);
        assertEquals(1,rowsUpdated);
        assertEquals(user.getUpdatedAt().getMinute(),LocalDateTime.now().getMinute());
        verify(userRepo,times(1)).updateUserRole(31L,Role.ADMIN);
        verify(userRepo,times(1)).save(user);
    }
    @Test
    void notAValidRoleToUpdate() throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class,()->userService.updateUserRole(31L, Role.valueOf("Developer")));
        verify(userRepo,never()).save(user);
    }


    @Test
    void updateUserStatus() throws UserNotFoundException {
        when(userRepo.existsById(31L)).thenReturn(true);
        when(userRepo.findById(31L)).thenReturn(Optional.ofNullable(user));
        when(userRepo.updateUserStatus(31L,"ACTIVE")).thenReturn(1);
        int rowsUpdated= userService.updateUserStatus(31L,"ACTIVE");
        assertEquals(1,rowsUpdated);
        assertEquals(user.getUpdatedAt().getMinute(),LocalDateTime.now().getMinute());
        verify(userRepo,times(1)).updateUserRole(31L,Role.ADMIN);
        verify(userRepo,times(1)).save(user);
    }
    @Test
    void noUserFoundToUpdateTheStatus() throws UserNotFoundException {
        when(userRepo.existsById(31L)).thenReturn(false);
        assertThrows(UserNotFoundException.class,()->userService.updateUserStatus(31L,"ACTIVE"));
        verify(userRepo,never()).updateUserStatus(31L,"ACTIVE");
        verify(userRepo,never()).save(user);
    }
    @Test
    void notAValidStatusToUpdate() throws UserNotFoundException {
        when(userRepo.existsById(31L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class,()->userService.updateUserStatus(31L,"DORMANT"));
        verify(userRepo,never()).updateUserStatus(31L,"DORMANT");
        verify(userRepo,never()).save(user);
    }

}
