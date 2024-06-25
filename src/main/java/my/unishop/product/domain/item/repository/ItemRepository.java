package my.unishop.product.domain.item.repository;

import my.unishop.product.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.itemName LIKE %:name%")
    Page<Item> findByItemNameContaining(String name, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId AND i.itemName LIKE %:name%")
    Page<Item> findByCategoryIdAndItemNameContaining(Long categoryId, String name, Pageable pageable);

    @Query("SELECT i FROM Item i")
    Page<Item> findAllWithImagesAndCategory(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Item findItemById(Long id);
}