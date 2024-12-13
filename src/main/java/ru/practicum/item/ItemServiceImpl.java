package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItemsForUser(long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(long itemId) {
        Item item = itemRepository.getItem(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto addItem(Item item, long userId) {
        Item savedItem = itemRepository.save(item, userId);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(long itemId, Item item, long userId) {
        Item updatedItem = itemRepository.updateItem(itemId, item, userId);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public void deleteItemByUser(long userId, long itemId) {
        itemRepository.deleteItemByUser(userId, itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemRepository.searchItems(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
