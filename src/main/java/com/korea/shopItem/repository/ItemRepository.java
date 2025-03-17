package com.korea.shopItem.repository;


import com.korea.shopItem.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT i FROM Item i WHERE i.delFlag = false")
    Page<Item> findAllWithImages(Pageable pageable);

}


/*
    Jpa에서 제공, 메서드 네이밍 규칙
    - 쿼리를 작성하지 않아도 메서드 이름 기반으로 쿼리를 실행해줌.

    ~필드 기준으로 조회
    조회 : findBy ( readBy~,  getBy~,  queryBy~)
    갯수 : countBy~ (Long 반환값)
    존재여부 : existsBy~ (Boolean 반환값)
    삭제 : deleteBy~ (void)

    List<User> findByName(String name);  // name으로 검색
    boolean existsByEmail(String email); // email 존재 여부 확인
    long countByAge(int age);            // 특정 age의 개수 조회
    void deleteById(Long id);            // 특정 id 삭제



 */









