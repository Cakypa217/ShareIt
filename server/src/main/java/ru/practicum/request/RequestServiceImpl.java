package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.RequestNotFoundException;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.ItemDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.model.ItemRequestDto;
import ru.practicum.user.UserService;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId) {
        log.info("Добавление нового запроса для пользователя с userId: {} с данными: {}", userId, itemRequestDto);
        User user = userService.findUserById(userId);
        ItemRequest itemRequest = RequestMapper.toItemRequest(itemRequestDto, user);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = requestRepository.save(itemRequest);

        if (itemRequestDto.getItems() != null && !itemRequestDto.getItems().isEmpty()) {
            for (ItemDto itemDto : itemRequestDto.getItems()) {
                Item item = ItemMapper.toItem(itemDto);
                item.setRequest(savedRequest);
                itemRepository.save(item);
            }
            log.info("Сохранены связанные вещи для запроса с id: {}", savedRequest.getId());
        }

        ItemRequestDto result = RequestMapper.toRequestDto(savedRequest, itemRequestDto.getItems());
        log.info("Запрос успешно добавлен: {}", result);
        return result;
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        log.info("Получение запросов пользователя с userId: {}", userId);
        userService.findUserById(userId);
        List<ItemRequestDto> requests = requestRepository.findByRequestorId(userId).stream()
                .map(request -> {
                    List<ItemDto> items = itemRepository.findByRequestId(request.getId()).stream()
                            .map(ItemMapper::toDto)
                            .collect(Collectors.toList());
                    return RequestMapper.toRequestDto(request, items);
                })
                .collect(Collectors.toList());
        log.info("Найдено {} запросов для пользователя с userId: {}", requests.size(), userId);
        return requests;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId) {
        log.info("Получение всех запросов кроме пользователя с userId: {}", userId);
        userService.findUserById(userId);
        List<ItemRequestDto> requests = requestRepository.findAllExceptUserId(userId).stream()
                .map(request -> {
                    List<ItemDto> items = itemRepository.findByRequestId(request.getId()).stream()
                            .map(ItemMapper::toDto)
                            .collect(Collectors.toList());
                    return RequestMapper.toRequestDto(request, items);
                })
                .collect(Collectors.toList());
        log.info("Найдено {} запросов (исключая запросы пользователя с userId: {})", requests.size(), userId);
        return requests;
    }

    @Override
    public ItemRequestDto getRequestById(long requestId, long userId) {
        log.info("Получение запроса с requestId: {} для пользователя с userId: {}", requestId, userId);
        userService.findUserById(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос с ID " + requestId + " не найден"));
        User requestor = userService.findUserById(itemRequest.getRequestor().getId());
        itemRequest.setRequestor(requestor);

        List<ItemDto> items = itemRepository.findByRequestId(requestId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
        ItemRequestDto result = RequestMapper.toRequestDto(itemRequest, items);
        log.info("Запрос найден: {}", result);
        return result;
    }
}
