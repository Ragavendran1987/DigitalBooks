package com.digitalBooks.digitalBooks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import com.digitalBook.DigitalBookApplication;
import com.digitalBook.controllers.DigitalBookController;
import com.digitalBook.models.Book;
import com.digitalBook.models.Payment;
import com.digitalBook.repository.BookRepository;
import com.digitalBook.repository.PaymentRepository;
import com.digitalBook.repository.RoleRepository;
import com.digitalBook.repository.UserRepository;

@SpringBootTest(classes = DigitalBookApplication.class)
@RunWith(MockitoJUnitRunner.class)
public class DigitalBooksApplicationTests {
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	RoleRepository roleRepository;
	
	@Mock
	BookRepository bookRepository;
	
	@Mock
	PaymentRepository paymentRepository;
	
	@InjectMocks
	DigitalBookController controller;
	
	@Before
	public void init() {
		Book testBook=new Book(1,"test book", "test", null, (float) 25.0,
				"MoonPublisher", true, new java.sql.Timestamp (System.currentTimeMillis ()), "test","2");
		when(bookRepository.findById(1)).thenReturn(Optional.of(testBook));
		when(bookRepository.save(ArgumentMatchers.anyObject())).thenReturn(testBook);
		Payment payment=new Payment(1, 1, "1");
		when(paymentRepository.save(ArgumentMatchers.anyObject())).thenReturn(payment);
		
		
	}
	
	@Test
	public void testGetBook() {
	 Optional<Book> testBook=bookRepository.findById(1);
	 
	   assertEquals("test book", testBook.get().getBookTitle());
	}
	@Test
	public void testCreateBook() {
		Book testBook=new Book(1,"test book", "test", null, (float) 25.0,
				"MoonPublisher", true, new java.sql.Timestamp (System.currentTimeMillis ()), "test","2");
	 Book result=bookRepository.save(testBook);
	   assertEquals("test book", result.getBookTitle());
	}
	
	@Test
	public void testCreatePayment() {
		Payment payment=new Payment(1, 1, "1");
		Payment result=paymentRepository.save(payment);
	   assertEquals("1", result.getUserId());
	}
	
	@Test
	public void testSearchController() {
	 ResponseEntity<?> testBook=controller.searchBooks("1");
	 assertEquals(true, testBook.hasBody());
	 
	}
	
	@Test
	public void testSearchUserController() {
	 ResponseEntity<?> testBook=controller.searchByUserID("1");
	 assertEquals(true, testBook.hasBody());
	 
	}
	
	@Test
	public void testCreateBookController() {
		Book testBook=new Book(1,"test book", "test", null, (float) 25.0,
				"MoonPublisher", true, new java.sql.Timestamp (System.currentTimeMillis ()), "test","2");
	 ResponseEntity<?> testBookResult=controller.createBooks("1",testBook );
	 assertEquals(true, testBookResult.hasBody());
	}
	@Test
	public void testUpdateBookController() {
		Book testBook=new Book(1,"test book", "test", null, (float) 25.0,
				"SunPublisher", true, new java.sql.Timestamp (System.currentTimeMillis ()), "test","2");
	 ResponseEntity<?> testBookResult=controller.updateBooks("1",testBook );
	 assertEquals(true, testBookResult.hasBody());
	}
	
	@Test
	public void testCreatePaymentController() {
		ResponseEntity<?> result=controller.doPayment("1", "1");
	   assertEquals(true, result.hasBody());
	}
	@Test
	public void testSearchPurchasedController() {
		ResponseEntity<?> result=controller.searchByUserID("1");
	   assertEquals(true, result.hasBody());
	}
}
