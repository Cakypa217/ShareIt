package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT DISTINCT r FROM ItemRequest r LEFT JOIN FETCH r.requestor WHERE r.requestor.id = :userId " +
            "ORDER BY r.created DESC")
    List<ItemRequest> findByRequestorId(@Param("userId") long userId);

    @Query("SELECT DISTINCT r FROM ItemRequest r LEFT JOIN FETCH r.requestor WHERE r.requestor.id <> :userId " +
            "ORDER BY r.created DESC")
    List<ItemRequest> findAllExceptUserId(@Param("userId") long userId);

}
