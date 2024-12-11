package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.model.ItemDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> getItemsForUser(@RequestHeader("X-Sharer-User-Id") long userId) {
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
    public ItemDto addItem(@Validated @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос POST /items с userId: {} и itemDto: {}", userId, itemDto);
        ItemDto item = itemService.addItem(itemDto, userId);
        log.info("Добавлен новый элемент: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос PATCH /items/{} с userId: {} и itemDto: {}", itemId, userId, itemDto);
        ItemDto item = itemService.updateItem(itemId, itemDto, userId);
        log.info("Обновлен элемент: {}", item);
        return item;
    }

    @DeleteMapping("/{userId}/{itemId}")
    public void deleteItemByUserId(@PathVariable long userId, @PathVariable long itemId) {
        log.info("Получен запрос DELETE /items/{}/{}", userId, itemId);
        itemService.deleteItemByUser(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос GET /items/search?text={}", text);
        List<ItemDto> items = itemService.searchItems(text);
        log.info("Найдены элементы: {}", items);
        return items;
    }
}
