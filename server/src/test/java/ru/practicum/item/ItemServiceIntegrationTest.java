package ru.practicum.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.model.ItemDto;
import ru.practicum.request.RequestService;
import ru.practicum.request.model.ItemRequestDto;
import ru.practicum.user.UserService;
import ru.practicum.user.model.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Test
    public void testCreateAndGetItem() {
        UserDto owner = new UserDto();
        owner.setName("Owner User");
        owner.setEmail("owner@example.com");
        UserDto savedOwner = userService.addUser(owner);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Ищу горный велосипед");
        ItemRequestDto savedRequest = requestService.addRequest(requestDto, savedOwner.getId());

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Велосипед");
        itemDto.setDescription("Горный велосипед");
        itemDto.setAvailable(true);
        itemDto.setRequestId(savedRequest.getId());

        ItemDto savedItem = itemService.addItem(itemDto, savedOwner.getId(), savedRequest.getId());

        assertNotNull(savedItem, "Сохраненная вещь не должна быть null");
        assertNotNull(savedItem.getId(), "ID сохраненной вещи не должен быть null");
        assertEquals(itemDto.getName(), savedItem.getName(), "Название вещи должно совпадать");
        assertEquals(itemDto.getDescription(), savedItem.getDescription(), "Описание вещи должно совпадать");
        assertTrue(savedItem.getAvailable(), "Вещь должна быть доступна");
        assertEquals(savedRequest.getId(), savedItem.getRequestId(), "ID запроса должен совпадать");

        List<ItemDto> ownerItems = itemService.getItemsForUser(savedOwner.getId());

        assertFalse(ownerItems.isEmpty(), "Список вещей не должен быть пустым");
        assertEquals(1, ownerItems.size(), "Должна быть одна вещь");
        assertEquals(savedItem.getId(), ownerItems.get(0).getId(), "ID вещей должны совпадать");
        assertEquals(savedRequest.getId(), ownerItems.get(0).getRequestId(), "ID запроса должен совпадать");
    }
}
