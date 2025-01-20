package ru.practicum.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.model.ItemRequestDto;
import ru.practicum.user.UserService;
import ru.practicum.user.model.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RequestServiceIntegrationTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateAndGetRequest() {
        UserDto requestor = new UserDto();
        requestor.setName("Requestor User");
        requestor.setEmail("requestor@example.com");
        UserDto savedRequestor = userService.addUser(requestor);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен велосипед для поездок по городу");

        ItemRequestDto savedRequest = requestService.addRequest(requestDto, savedRequestor.getId());

        assertNotNull(savedRequest, "Сохраненный запрос не должен быть null");
        assertNotNull(savedRequest.getId(), "ID сохраненного запроса не должен быть null");
        assertEquals(requestDto.getDescription(), savedRequest.getDescription(),
                "Описание запроса должно совпадать");
        assertNotNull(savedRequest.getCreated(), "Дата создания не должна быть null");

        List<ItemRequestDto> userRequests = requestService.getUserRequests(savedRequestor.getId());

        assertFalse(userRequests.isEmpty(), "Список запросов не должен быть пустым");
        assertEquals(1, userRequests.size(), "Должен быть один запрос");
        assertEquals(savedRequest.getId(), userRequests.get(0).getId(), "ID запросов должны совпадать");
        assertEquals(savedRequest.getDescription(), userRequests.get(0).getDescription(),
                "Описания запросов должны совпадать");
    }
}
