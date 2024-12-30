package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :bookerId AND b.status = :status" +
            " AND b.end < :endDate")
    List<Booking> findPastBookings(@Param("itemId") Long itemId, @Param("bookerId") Long bookerId,
                                   @Param("status") Status status, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now ORDER BY b.start DESC")
    Booking findLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now ORDER BY b.start ASC")
    Booking findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);
}
