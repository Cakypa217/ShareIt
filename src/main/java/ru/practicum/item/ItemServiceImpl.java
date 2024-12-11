package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.item.model.ItemDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItemsForUser(long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        return itemRepository.save(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        return itemRepository.updateItem(itemId, itemDto, userId);
    }

    @Override
    public void deleteItemByUser(long userId, long itemId) {
        itemRepository.deleteItemByUser(userId, itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}
