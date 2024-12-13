package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> getItemsForUser(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Получен запрос GET /items/{}", userId);
        List<ItemDto> items = itemService.getItemsForUser(userId);
        log.info("Найдены элементы: {}", items);
        return items;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        log.info("Получен запрос GET /items/{}", itemId);
        ItemDto item = itemService.getItem(itemId);
        log.info("Найден элемент: {}", item);
        return item;
    }

    @PostMapping()
    public ItemDto addItem(@Validated @RequestBody Item item, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Получен запрос POST /items с userId: {} и itemDto: {}", userId, item);
        ItemDto itemDto = itemService.addItem(item, userId);
        log.info("Добавлен новый элемент: {}", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос PATCH /items/{} с userId: {} и itemDto: {}", itemId, userId, item);
        ItemDto itemDto = itemService.updateItem(itemId, item, userId);
        log.info("Обновлен элемент: {}", itemDto);
        return itemDto;
    }

    @DeleteMapping("/{userId}/{itemId}")
    public void deleteItemByUserId(@PathVariable long userId, @PathVariable long itemId) {
        log.info("Получен запрос DELETE /items/{}/{}", userId, itemId);
        itemService.deleteItemByUser(userId, itemId);
        log.info("Элемент с id {} успешно удален пользователем с id {}", itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос GET /items/search?text={}", text);
        List<ItemDto> items = itemService.searchItems(text);
        log.info("Найдены элементы: {}", items);
        return items;
    }
}
