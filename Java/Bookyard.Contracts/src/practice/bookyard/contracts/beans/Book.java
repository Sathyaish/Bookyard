package practice.bookyard.contracts.beans;

public class Book {
	
	// id
	// name
	// author
	// description
	// amazonUrl
	
	private int id;
	private String name;
	private String author;
	private String description;
	private String amazonUrl;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAmazonUrl() {
		return amazonUrl;
	}
	public void setAmazonUrl(String amazonUrl) {
		this.amazonUrl = amazonUrl;
	}
}
