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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Kullanıcı İşlemleri", description = "Kullanıcı oluşturma, silme, güncelleme ve listeleme işlemleri")
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
		this.passwordEncoder = new BCryptPasswordEncoder(); // Şifreleme mekanizması
	}

	@Operation(summary = "Yeni kullanıcı kaydet", description = "Yeni bir kullanıcı oluşturur. Eğer aynı e-posta ile kullanıcı varsa, hata döner.")
	@PostMapping(path = "/save")
	public ResponseEntity<?> createUser(@RequestBody UserRegistrationDTO registrationDTO) {
		// Aynı email ile kullanıcı var mı kontrol et
		Optional<User> existingUser = userService.getUserByEmail(registrationDTO.getEmail());
		if (existingUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Bu e-posta adresi zaten kayıtlı: " + registrationDTO.getEmail());
		}

		// Şifreyi şifreliyoruz BCrypt hashing ile
		String encryptedPassword = passwordEncoder.encode(registrationDTO.getPassword());

		// DTO -> Entity
		User user = new User(null, registrationDTO.getUsername(), registrationDTO.getEmail(), encryptedPassword);

		// Kaydet
		User createdUser = userService.SaveUser(user);

		// Geriye DTO dön
		UserDTO createdUserDTO = new UserDTO(createdUser.getUsername(), createdUser.getEmail());
		return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
	}

	@Operation(summary = "Tüm kullanıcıları getir", description = "Veritabanındaki tüm kullanıcıları listeler.")
	@GetMapping
	public List<UserDTO> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return users.stream().map(user -> new UserDTO(user.getUsername(), user.getEmail()))
				.collect(Collectors.toList());
	}

	@Operation(summary = "ID ile kullanıcıyı getir", description = "Verilen ID'ye sahip kullanıcıyı getirir.")
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.getUserById(id);
		return user.map(u -> ResponseEntity.ok(new UserDTO(u.getUsername(), u.getEmail())))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yapar.")
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserRegistrationDTO loginDTO) {
		Optional<User> userOptional = userService.getUserByUsername(loginDTO.getUsername());

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
				return ResponseEntity.ok("Giriş başarılı. Hoş geldin " + user.getUsername());
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Şifre yanlış.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı.");
		}
	}

	@Operation(summary = "Kullanıcı sil", description = "Verilen ID'ye sahip kullanıcıyı siler.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
