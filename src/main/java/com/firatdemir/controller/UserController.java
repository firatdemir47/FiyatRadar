package com.firatdemir.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.firatdemir.dto.UserDTO;
import com.firatdemir.dto.UserRegistrationDTO;
import com.firatdemir.model.User;
import com.firatdemir.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
		this.passwordEncoder = new BCryptPasswordEncoder(); // Şifreleme mekanizması
	}

	// Kullanıcı ekleme
	@PostMapping(path = "/save")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserRegistrationDTO registrationDTO) {
	    // DTO'dan şifreyi alıyoruz
	    String password = registrationDTO.getPassword();

	    // Şifreyi şifreliyoruz
	    String encryptedPassword = passwordEncoder.encode(password);

	    // DTO'yu Entity'ye çeviriyoruz
	    User user = new User(null, registrationDTO.getUsername(), registrationDTO.getEmail(), encryptedPassword);

	    // Kullanıcıyı kaydediyoruz
	    User createdUser = userService.SaveUser(user);

	    // Geriye döndürülecek DTO'yu oluşturuyoruz (şifre içermez)
	    UserDTO createdUserDTO = new UserDTO(createdUser.getUsername(), createdUser.getEmail());
	    return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
	}

	@GetMapping
	public List<UserDTO> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return users.stream().map(user -> new UserDTO(user.getUsername(), user.getEmail()))
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.getUserById(id);
		return user.map(u -> ResponseEntity.ok(new UserDTO(u.getUsername(), u.getEmail())))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
