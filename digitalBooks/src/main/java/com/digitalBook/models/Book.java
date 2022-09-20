package com.digitalBook.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer bookId;
	@Column
	private String bookTitle;
	@Column
	private String category;
	@Column
	private String bookLogo;
	@Column
	private float bookPrice;
	@Column
	private String bookPublisher;
	@Column(columnDefinition = "boolean default false")
	private Boolean activeFlag ;
	@Column
	private Timestamp publishedDate ;
	@Column
	private String bookContent ;	
	@Column
	private String authorId ;
	
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBookLogo() {
		return bookLogo;
	}
	public void setBookLogo(String bookLogo) {
		this.bookLogo = bookLogo;
	}
	public float getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(float bookPrice) {
		this.bookPrice = bookPrice;
	}
	public String getBookPublisher() {
		return bookPublisher;
	}
	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}
	public Boolean getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}
	public Timestamp getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(Timestamp publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getBookContent() {
		return bookContent;
	}
	public void setBookContent(String bookContent) {
		this.bookContent = bookContent;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public Book(Integer bookId, String bookTitle, String category, String bookLogo, float bookPrice,
			String bookPublisher, Boolean activeFlag, Timestamp publishedDate, String bookContent, String authorId) {
		super();
		this.bookId = bookId;
		this.bookTitle = bookTitle;
		this.category = category;
		this.bookLogo = bookLogo;
		this.bookPrice = bookPrice;
		this.bookPublisher = bookPublisher;
		this.activeFlag = activeFlag;
		this.publishedDate = publishedDate;
		this.bookContent = bookContent;
		this.authorId = authorId;
	}
	
	public  Book() {
		
	}
	
	
}
