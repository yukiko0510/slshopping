package com.example.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.security.SLShopUserDetails;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 管理者一覧画面表示
     *
     * @param model
     * @return 管理者一覧画面
     */
    @GetMapping
    public String listUsers(@RequestParam(required = false) String keyword, Model model) {
        // 全管理者情報の取得
        List<User> listUsers = userService.listAll(keyword);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("keyword", keyword);
        return "users/users";
    }

    /**
     * 管理者新規登録画面表示
     *
     * @param model
     * @return 管理者新規登録画面
     */
    @GetMapping("/new")
    public String newUser(Model model) {
        // 新規登録用に、空の管理者情報作成
        User user = new User();
        // 全ロール情報の取得
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "users/user_form";
    }

    /**
     * 管理者登録・更新処理
     *
     * @param user 管理者情報
     * @param ra
     * @return 管理者一覧画面
     */
    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes ra) {
        // 管理者情報の登録
        userService.save(user);
        // 登録成功のメッセージを格納
        ra.addFlashAttribute("success_message", "登録に成功しました");
        return "redirect:/users";
    }

    /**
     * 管理者詳細画面表示
     *
     * @param id 管理者ID
     * @param model
     * @return 管理者詳細画面
     */
    @GetMapping("/detail/{id}")
    public String detailUser(@PathVariable(name = "id") Long id, Model model) {
        // 管理者IDに紐づく管理者情報取得
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "users/user_detail";
    }

    /**
     * 管理者編集画面表示
     *
     * @param id 管理者ID
     * @param model
     * @return 管理者編集画面
     */
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model) {
        // 管理者IDに紐づく管理者情報取得
        User user = userService.get(id);
        // 全ロール情報の取得
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "users/user_edit";
    }

    /**
     * 管理者削除処理
     *
     * @param id 管理者ID
     * @param userDetails ログイン者情報
     * @param model
     * @param ra
     * @return 管理者一覧画面
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id, @AuthenticationPrincipal SLShopUserDetails userDetails, Model model, RedirectAttributes ra) {
        // 削除対象が、ログイン中の管理者自身だった場合は削除しない
        if (userDetails.getUser().getId().equals(id)) {
            // 削除失敗のメッセージを格納
            ra.addFlashAttribute("error_message", "削除に失敗しました");
            return "redirect:/users";
        }
        // 管理者情報削除
        userService.delete(id);
        // 削除成功のメッセージを格納
        ra.addFlashAttribute("success_message", "削除に成功しました");
        return "redirect:/users";
    }

}
