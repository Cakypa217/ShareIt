package ru.practicum.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.item.model.ItemDto;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.model.ItemRequestDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ItemRequestDto toRequestDto(ItemRequest request, List<ItemDto> items) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setRequestor(UserMapper.toUserDto(request.getRequestor()));
        requestDto.setItems(items);
        return requestDto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto, User requestor) {
        ItemRequest request = new ItemRequest();
        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        request.setItems(new ArrayList<>());
        return request;
    }
}
