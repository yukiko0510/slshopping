package com.example.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Brand;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    /**
     * ブランド情報全件取得
     *
     * @return ブランド情報のリスト
     */
    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    /**
     * ブランド情報検索処理
     *
     * @param keyword 検索キーワード
     * @return ブランド情報のリスト
     */
    public List<Brand> listAll(String keyword) {
        // 検索キーワードがあった場合
        if (keyword != null && !keyword.isEmpty()) {
            return brandRepository.search(keyword);
        }
        // それ以外の場合
        else {
            return brandRepository.findAll();
        }
    }

    /**
     * IDに紐づくブランド情報取得処理
     *
     * @param id ブランドID
     * @return ブランド情報
     */
    public Brand get(Long id) {
        return brandRepository.findById(id).get();
    }

    /**
     * ブランド情報登録処理
     *
     * @param brand 保存したいブランド情報
     * @return 保存したブランド情報
     */
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    /**
     * IDに紐づくブランド情報削除処理
     *
     * @param id ブランドID
     */
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }

}
