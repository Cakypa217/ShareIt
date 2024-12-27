package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByItemIdAndBookerIdAndStatusAndEndBefore(
            Long itemId, Long bookerId, Status status, LocalDateTime endDate
    );

    Booking findFirstByItemIdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);
}
