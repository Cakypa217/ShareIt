package ru.practicum.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final HashMap<Long, List<Item>> items = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public List<Item> findByUserId(long userId) {
        return items.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public Item save(Item item) {
        item.setId(idGenerator.incrementAndGet());
        items.computeIfAbsent(item.getUserId(), k -> new ArrayList<>()).add(item);
        return item;
    }


    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        List<Item> userItems = items.get(userId);
        if (userItems != null) {
            userItems.removeIf(item -> item.getId() == itemId);
        }
    }
}

