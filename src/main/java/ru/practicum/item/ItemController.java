package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.model.CommentDto;
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
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId, @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getItem(itemId, userId);
    }

    @PostMapping()
    public ItemDto addItem(@Validated @RequestBody ItemDto itemDto, @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @DeleteMapping("/{userId}/{itemId}")
    public void deleteItemByUserId(@PathVariable long userId, @PathVariable long itemId) {
        itemService.deleteItemByUser(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId, @Validated @RequestBody CommentDto commentDto,
                                 @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.addComment(itemId, commentDto, userId);
    }
}
