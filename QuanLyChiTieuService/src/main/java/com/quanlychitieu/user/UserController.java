package com.quanlychitieu.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlychitieu.common.user.User;

@RestController
@RequestMapping("/api/quanlychitieu/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public ResponseEntity<?> loadAllUser() {
		List<User> users = userService.loadAll();
		if (users.size() > 0) {
            System.out.println(users);
			return ResponseEntity.ok(users);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
}
