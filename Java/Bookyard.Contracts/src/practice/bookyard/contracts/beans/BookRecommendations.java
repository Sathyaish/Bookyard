package practice.bookyard.contracts.beans;

import java.util.ArrayList;
import java.util.List;

public class BookRecommendations {
	private int userId;
	private String userName;
	private List<Book> books;
	
	public BookRecommendations() {
		this(0, null);
	}
	
	public BookRecommendations(int userId, String userName) {
		this.setUserId(userId);
		this.setUserName(userName);
		
		this.books = new ArrayList<Book>();
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public List<Book> getBooks() {
		return this.books;
	}
	
	public void addBook(Book book) {
		if (book == null) return;
		
		if (this.books.contains(book)) return;
		
		this.books.add(book);
	}
	
	public void removeBook(Book book) {
		if (book == null) return;
		
		if (this.books.contains(book)) {
			this.books.remove(book);
		}
	}
}
