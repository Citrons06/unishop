package my.unishop.product.domain.item.repository;

import my.unishop.product.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemNameContaining(String name);
    List<Item> findByCategoryId(Long categoryId);
    List<Item> findByCategoryIdAndItemNameContaining(Long categoryId, String name);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.itemImgList LEFT JOIN FETCH i.category")
    List<Item> findAllWithImagesAndCategory();

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.itemImgList WHERE i.id = :id")
    Item findItemAndItemImgById(Long id);

}