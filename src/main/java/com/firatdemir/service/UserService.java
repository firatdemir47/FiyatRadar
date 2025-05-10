package com.firatdemir.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firatdemir.model.User;
import com.firatdemir.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Kullanıcı ekleme
	public User SaveUser(User user) {
		return userRepository.save(user);
	}

	// Kullanıcı Listeleme
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Kullanıcı Bulma
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	// Kullanıcı Silme
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	// Eposta ile kullanıcıyı bul
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	// Kullanıcı adı ile kullanıcıyı bul
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
