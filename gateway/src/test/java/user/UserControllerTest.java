package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ShareItGateway;
import ru.practicum.user.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = ShareItGateway.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @Autowired
    private ObjectMapper mapper;

    private CreateUserDto createUserDto;
    private UpdateUserDto updateUserDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        createUserDto = CreateUserDto.builder()
                .name("Test User")
                .email("test@test.com")
                .build();

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Updated User");
        updateUserDto.setEmail("updated@test.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@test.com");
    }

    @Test
    void getUsers_ShouldReturnUsersList() throws Exception {
        List<UserDto> users = List.of(userDto);
        when(userClient.getUsers())
                .thenReturn(ResponseEntity.ok(users));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userClient).getUsers();
    }

    @Test
    void getUser_WithValidId_ShouldReturnUser() throws Exception {
        long userId = 1L;
        when(userClient.getUser(userId))
                .thenReturn(ResponseEntity.ok(userDto));

        mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userClient).getUser(userId);
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
        when(userClient.createUser(any(CreateUserDto.class)))
                .thenReturn(ResponseEntity.ok(userDto));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDto)))
                .andExpect(status().isOk());

        verify(userClient).createUser(any(CreateUserDto.class));
    }

    @Test
    void createUser_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        createUserDto.setEmail("invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        createUserDto.setName("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithValidData_ShouldReturnUpdatedUser() throws Exception {
        long userId = 1L;
        when(userClient.updateUser(eq(userId), any(UpdateUserDto.class)))
                .thenReturn(ResponseEntity.ok(userDto));

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk());

        verify(userClient).updateUser(eq(userId), any(UpdateUserDto.class));
    }

    @Test
    void updateUser_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        long userId = 1L;
        updateUserDto.setEmail("invalid-email");

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_WithValidId_ShouldReturnOk() throws Exception {
        long userId = 1L;
        when(userClient.deleteUser(userId))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userClient).deleteUser(userId);
    }
}
