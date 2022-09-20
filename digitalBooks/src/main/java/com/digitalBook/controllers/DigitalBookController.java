package com.digitalBook.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalBook.models.Book;
import com.digitalBook.models.ERole;
import com.digitalBook.models.Payment;
import com.digitalBook.models.Role;
import com.digitalBook.models.User;
import com.digitalBook.payload.request.LoginRequest;
import com.digitalBook.payload.request.SignupRequest;
import com.digitalBook.payload.response.JwtResponse;
import com.digitalBook.payload.response.MessageResponse;
import com.digitalBook.repository.BookRepository;
import com.digitalBook.repository.PaymentRepository;
import com.digitalBook.repository.RoleRepository;
import com.digitalBook.repository.UserRepository;
import com.digitalBook.security.jwt.JwtUtils;
import com.digitalBook.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/digitalBook/")
public class DigitalBookController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_READER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ROLE_AUTHOR":
					Role adminRole = roleRepository.findByName(ERole.ROLE_AUTHOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "ROLE_READER":
					Role modRole = roleRepository.findByName(ERole.ROLE_READER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_READER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@PostMapping("/{author}/createBooks")
public ResponseEntity<?> createBooks(@PathVariable String author, @RequestBody Book book) {
		book.setAuthorId(author);
		book.setPublishedDate(new java.sql.Timestamp (System.currentTimeMillis ()));
		bookRepository.save(book);
		return ResponseEntity.ok(new MessageResponse("Book saved successfully!"));
		
		
	}
@GetMapping("/searchBooks")
public ResponseEntity<?>searchBooks(@RequestParam String bookId) {
	Optional<Book> book=bookRepository.findById(Integer.parseInt(bookId));
	return ResponseEntity.ok(book);
	
	
}
@PreAuthorize("hasRole('ROLE_AUTHOR')")
@PutMapping("/{bookId}/updateBooks")
public ResponseEntity<?>updateBooks(@PathVariable String bookId, @RequestBody Book book) {
	Optional<Book> existingBook=bookRepository.findById(Integer.parseInt(bookId));
    Book updateBook=existingBook.get();
    if(null!=updateBook) {
    	updateBook.setActiveFlag(book.getActiveFlag());
    	updateBook.setBookContent(book.getBookContent());
    	updateBook.setBookPrice(book.getBookPrice());
    	updateBook.setBookPublisher(book.getBookPublisher());
    	updateBook.setBookTitle(book.getBookTitle());
    	updateBook.setCategory(book.getCategory());
    	bookRepository.save(updateBook);
    	
    }

	return ResponseEntity.ok(updateBook);
	
	
}

//@PreAuthorize("hasRole('ROLE_READER')")
@PostMapping("/{userId}/{bookId}/payment")
public ResponseEntity<?>doPayment(@PathVariable String userId, @PathVariable String bookId){
	Payment bookPayment= new Payment();
	bookPayment.setBookId(Integer.parseInt(bookId));
	bookPayment.setUserId(userId);
	Payment paymentStatus= paymentRepository.save(bookPayment);
	
	return ResponseEntity.ok("purchased successfully Payment ID!"+ paymentStatus.getPaymentId());
}

//@PreAuthorize("hasRole('ROLE_AUTHOR')")
@PostMapping("/{author}/getPurchasedBooks")
public ResponseEntity<?> searchByUserID(@PathVariable String author) {
	List<Payment> purchasedBooks= paymentRepository.findByUserId(author);
List<Book> paidBooks=new ArrayList<>();
for(Payment pay:purchasedBooks) {
	Optional<Book> book=bookRepository.findById(pay.getBookId());
	paidBooks.add(book.get());
}
			return ResponseEntity.ok(paidBooks);
	
}


}
