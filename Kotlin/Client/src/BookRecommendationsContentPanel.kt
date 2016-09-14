package bookyard.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bookyard.contracts.beans.Book;

public class BookRecommendationsContentPanel(val lblStatus : JLabel) : JPanel() {

    init {
        val gridLayout : GridLayout = GridLayout(0, 1);
        gridLayout.setVgap(10);
        gridLayout.setHgap(20);
        this.setLayout(gridLayout);
        this.setBorder(EmptyBorder(20, 20, 10, 20));
    }

    public fun AddBook(book : Book) {

        val pnlBook : JPanel = JPanel();
        val gridLayout : GridLayout = GridLayout(0, 1);
        gridLayout.setVgap(7);
        pnlBook.setLayout(gridLayout);

        val hyperlinkLabel : HyperLinkLabel = HyperLinkLabel(book.amazonUrl!!, book.name!!);

        // hyperlinkLabel.addMouseListener()

        pnlBook.add(hyperlinkLabel);

        val lblAuthor : JLabel = JLabel(book.author!!);
        lblAuthor.setFont(Font("Times New Roman", Font.ITALIC, 12));
        pnlBook.add(lblAuthor);

        // int parentWidth = this.getWidth();
        // String description = String.format("<html><body style = \"width: %dpx\">%s</body></html>", (int)(parentWidth * 0.95), book.getDescription());
        val lblDescription : JLabel = JLabel(book.description!!);
        lblDescription.setFont(Font("Times New Roman", Font.PLAIN, 12));
        pnlBook.add(lblDescription);

        this.add(pnlBook);
    }
}