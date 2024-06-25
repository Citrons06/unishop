package my.unishop.orders.domain.inventory.repository;

import my.unishop.orders.domain.inventory.entity.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
}
