package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.comments WHERE i.user.id = :userId")
    List<Item> findByUserId(@Param("userId") long userId);

    @Query(value = "SELECT * FROM items i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))", nativeQuery = true)
    List<Item> searchItems(@Param("text") String text);

    @Modifying
    @Query("DELETE FROM Item i WHERE i.id = :itemId AND i.user.id = :userId")
    void deleteItemByUser(@Param("userId") long userId, @Param("itemId") long itemId);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.request r WHERE r.id = :requestId")
    List<Item> findByRequestId(@Param("requestId") Long requestId);

}
