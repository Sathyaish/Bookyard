package bookyard.contracts.beans;

import java.util.ArrayList;

data class BookRecommendations(var userId : Int = 0,
                               var userName : String? = null,
                               val books : MutableList<Book?> = ArrayList<Book?>()) {

    fun addBook(book : Book?) {
        if (book == null) return;

        if (this.books.contains(book)) return;

        this.books.add(book);
    }

    fun removeBook(book : Book?) {
        if (book == null) return;

        if (this.books.contains(book)) {
            this.books.remove(book);
        }
    }
}