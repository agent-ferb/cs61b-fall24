import org.junit.jupiter.api.*;

import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MaxArrayDeque61BTest {
    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    @Test
    public void basicTest() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new StringLengthComparator());
        mad.addFirst("");
        mad.addFirst("2");
        mad.addFirst("fury road");
        assertThat(mad.max()).isEqualTo("fury road");
    }

    @Test
    public void max_default() {
        Comparator<Integer> naturalOrder = Comparator.naturalOrder();
        MaxArrayDeque61B<Integer> maxDeque = new MaxArrayDeque61B<>(naturalOrder);

        maxDeque.addLast(3);
        maxDeque.addLast(1);
        maxDeque.addLast(4);
        maxDeque.addLast(2);

        assertEquals(Integer.valueOf(4), maxDeque.max());
    }

    @Test
    public void max_different_comp() {
        Comparator<Integer> naturalOrder = Comparator.naturalOrder();
        MaxArrayDeque61B<Integer> maxDeque = new MaxArrayDeque61B<>(naturalOrder);

        maxDeque.addLast(3);
        maxDeque.addLast(1);
        maxDeque.addLast(4);
        maxDeque.addLast(2);

        Comparator<Integer> reverseOrder = Comparator.reverseOrder();

        assertEquals(Integer.valueOf(1), maxDeque.max(reverseOrder));
    }

    @Test
    public void max_empty() {
        Comparator<Integer> naturalOrder = Comparator.naturalOrder();
        MaxArrayDeque61B<Integer> maxDeque = new MaxArrayDeque61B<>(naturalOrder);

        assertNull(maxDeque.max());
    }

    @Test
    public void max_nonempty() {
        Comparator<Integer> naturalOrder = Comparator.naturalOrder();
        MaxArrayDeque61B<Integer> maxDeque = new MaxArrayDeque61B<>(naturalOrder);

        maxDeque.addLast(10);
        maxDeque.addLast(15);
        maxDeque.addLast(7);

        assertEquals(Integer.valueOf(15), maxDeque.max());
    }
}
