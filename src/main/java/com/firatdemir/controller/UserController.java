	package com.firatdemir.controller;
	
	import java.util.List;
	import java.util.Optional;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.DeleteMapping;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;
	

	import com.firatdemir.model.User;
	import com.firatdemir.service.UserService;
	
	

	@RestController
	@RequestMapping("/api/users")
	public class UserController {
	
		private final UserService userService;
	
		@Autowired
		public UserController(UserService userService) {
			this.userService = userService;
		}
	
		// Kullanıcı ekleme
		@PostMapping(path = "/save")
		public ResponseEntity<User> createUser(@RequestBody User user) {
			User createdUser = userService.SaveUser(user);
			return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
		}
		
		// Tüm kullanıcıları listeleme
		@GetMapping
		public List<User> getAllUsers() {
			return userService.getAllUsers();
		}
		
		//kullanıcı id ile getirme 
		@GetMapping("/{id}")
		public ResponseEntity<User> getUserById(@PathVariable Long id){
			Optional<User> user=userService.getUserById(id);
			return user.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
		}
		//kullanıcı silme 
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> deleteUser(@PathVariable Long id){
			userService.deleteUser(id);
			return ResponseEntity.noContent().build();
		}
	}
