package com.example.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.entity.User;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 管理者情報検索処理
     *
     * @param keyword 検索キーワード
     * @return 管理者情報のリスト
     */
    public List<User> listAll(String keyword) {
        // 検索キーワードがあった場合
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.search(keyword);
        }
        // それ以外の場合
        else {
            return userRepository.findAll();
        }
    }

    /**
     * ロール情報全件取得処理
     *
     * @return ロール情報のリスト
     */
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    /**
     * IDに紐づく管理者情報取得処理
     *
     * @param id 管理者ID
     * @return 管理者情報
     */
    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * 管理者情報登録処理
     *
     * @param user 保存したい管理者情報
     * @return 保存した管理者情報
     */
    public User save(User user) {
        // 管理者情報を更新する場合
        if (user.getId() != null) {
            // 更新対象の管理者情報を取得
            User existingUser = userRepository.findById(user.getId()).get();
            // 保存したい管理者情報のパスワードが空の場合
            if (user.getPassword().isEmpty()) {
                // 保存したい管理者情報に以前のパスワードを格納
                user.setPassword(existingUser.getPassword());
            } else {
                // パスワードのハッシュ化
                String encodedPassword = encodePassword(user.getPassword());
                // ハッシュ化したパスワードを格納
                user.setPassword(encodedPassword);
            }
        }
        // 管理者情報を新規登録する場合
        else {
            // パスワードのハッシュ化
            String encodedPassword = encodePassword(user.getPassword());
            // ハッシュ化したパスワードを格納
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    /**
     * パスワードのハッシュ化
     *
     * @param rawPassword
     */
    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * IDに紐づく管理者情報削除処理
     *
     * @param id 管理者ID
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
