package ru.practicum.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserModelTest {
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@mail.com");
        user1.setName("User1");

        user2 = new User();
        user2.setId(1L);
        user2.setEmail("user1@mail.com");
        user2.setName("User1");

        user3 = new User();
        user3.setId(2L);
        user3.setEmail("user2@mail.com");
        user3.setName("User2");
    }

    @Test
    void testEquals() {
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void testHashCode() {
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setName("Test User");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getName()).isEqualTo("Test User");
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertThat(user).isNotNull();
    }
}
