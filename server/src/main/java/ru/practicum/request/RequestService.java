package ru.practicum.request;

import ru.practicum.request.model.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> getUserRequests(long userId);

    List<ItemRequestDto> getAllRequests(long userId);

    ItemRequestDto getRequestById(long requestId, long userId);
}
