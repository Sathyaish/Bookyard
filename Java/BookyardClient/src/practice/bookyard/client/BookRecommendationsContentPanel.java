package practice.bookyard.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import practice.bookyard.contracts.beans.Book;

public class BookRecommendationsContentPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel lblStatus = null;
	
	public BookRecommendationsContentPanel(JLabel lblStatus) {
		super();
		
		this.lblStatus = lblStatus;
		
		GridLayout gridLayout = new GridLayout(0, 1);
		gridLayout.setVgap(10);
		gridLayout.setHgap(20);
		this.setLayout(gridLayout);
		this.setBorder(new EmptyBorder(20, 20, 10, 20));
	}
	
	public void AddBook(Book book) {
		
		JPanel pnlBook = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 1);
		gridLayout.setVgap(7);
		pnlBook.setLayout(gridLayout);
		
		HyperLinkLabel hyperlinkLabel = new HyperLinkLabel(book.getAmazonUrl(), book.getName());
		
		// hyperlinkLabel.addMouseListener()
		
		pnlBook.add(hyperlinkLabel);
		
		JLabel lblAuthor = new JLabel(book.getAuthor());
		lblAuthor.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		pnlBook.add(lblAuthor);
		
		// int parentWidth = this.getWidth();
		// String description = String.format("<html><body style = \"width: %dpx\">%s</body></html>", (int)(parentWidth * 0.95), book.getDescription());
		JLabel lblDescription = new JLabel(book.getDescription());
		lblDescription.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		pnlBook.add(lblDescription);
		
		this.add(pnlBook);
	}
}
