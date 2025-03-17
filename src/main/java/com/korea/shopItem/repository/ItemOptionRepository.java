package com.korea.shopItem.repository;

import com.korea.shopItem.domain.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    List<ItemOption> findByItemId(Long itemId);
}
