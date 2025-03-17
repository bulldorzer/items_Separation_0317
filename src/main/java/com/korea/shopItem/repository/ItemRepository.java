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

    // 현재 이메소드는 아이템정보와 이미지목록을 같이 가져옴
    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT i FROM Item i WHERE i.delFlag = false") // 삭제된거 제외
    Page<Item> findAllWithImages(Pageable pageable);

    /*
    * findById(아이템id)
    * 이미지 같이 안옴
    * 옵션 같이 안옴
    * 인포 같이 옴( Iteminfo는 값타입이기때문)
    */
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