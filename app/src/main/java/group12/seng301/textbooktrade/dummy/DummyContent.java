package group12.seng301.textbooktrade.dummy;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group12.seng301.textbooktrade.TextbooksDatabaseHelper;
import group12.seng301.textbooktrade.objects.Book;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static void setContext(Context c) {
        TextbooksDatabaseHelper databaseHelper = TextbooksDatabaseHelper.getInstance(c);
        System.out.println(databaseHelper.getAllBooks().size());
        int i = 0;
        for (Book b : databaseHelper.getAllBooks()) {
            addItem(createDummyItem(i, b));
            i++;
        }

    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position, Book book) {
        return new DummyItem(String.valueOf(position), book);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final Book book;

        public DummyItem(String id, Book book) {
            this.id = id;
            this.book = book;
        }

        @Override
        public String toString() {
            return book.getName();
        }
    }
}
