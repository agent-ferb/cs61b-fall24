import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import java.util.Iterator;
import static org.junit.Assert.*;


public class LinkedListDeque61BTest {

    @Test
    public void testEquals_sameObject() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        assertThat(ad1.equals(ad1)).isTrue(); // Same object should be equal
    }

    @Test
    public void testEquals_differentOrder() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad2.addLast(2);
        ad2.addLast(1);
        assertThat(ad1.equals(ad2)).isFalse(); // Different order, should return false
    }

    @Test
    public void testEquals_sameElements() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad2.addLast(1);
        ad2.addLast(2);
        assertThat(ad1.equals(ad2)).isTrue(); // Same elements in the same order
    }

    @Test
    public void testEquals_differentSize() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad2.addLast(1);
        ad2.addLast(2);
        assertThat(ad1.equals(ad2)).isFalse(); // Different size
    }

    @Test
    public void testEquals_differentTypes() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ArrayDeque61B<String> stringDeque = new ArrayDeque61B<>();

        stringDeque.addLast("1");
        assertThat(ad1.equals(stringDeque)).isFalse(); // Different types
    }

    @Test
    public void testToString_nonEmpty() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        assertEquals("[1, 2, 3]", ad1.toString()); // Check correct format for non-empty deque
    }

    @Test
    public void testToString_empty() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        assertEquals("[]", ad1.toString()); // Empty deque should return {}
    }

    @Test
    public void testIterator() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);

        Iterator<Integer> it = ad1.iterator();

        assertThat(it.hasNext()).isTrue();
        assertEquals(Integer.valueOf(1), it.next());

        assertThat(it.hasNext()).isTrue();
        assertEquals(Integer.valueOf(2), it.next());

        assertThat(it.hasNext()).isTrue();
        assertEquals(Integer.valueOf(3), it.next());

        assertThat(it.hasNext()).isFalse(); // No more elements left
    }
    @Test
    public void testIterator_emptyDeque() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        Iterator<Integer> it = ad1.iterator();
        assertThat(it.hasNext()).isFalse(); // Empty deque should have no next element
    }

    /** Tests for checking that different degues can be equal to each other. */
    @Test
    public void testEqualLinkedAndArrayDeques() {
        Deque61B<String> ad = new ArrayDeque61B<>();
        Deque61B<String> lld = new LinkedListDeque61B<>();
        ad.addLast("front");
        ad.addLast("middle");
        ad.addLast("back");
        lld.addLast("front");
        lld.addLast("middle");
        lld.addLast("back");
        assertThat(lld).isEqualTo(ad);
        assertThat(ad).isEqualTo(lld);
    }
}

