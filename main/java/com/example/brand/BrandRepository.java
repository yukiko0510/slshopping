package com.example.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    /**
     * ブランド情報検索クエリ
     *
     * @param keyword 検索キーワード
     * @return ブランド情報のリスト
     */
    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    public List<Brand> search(String keyword);

}
