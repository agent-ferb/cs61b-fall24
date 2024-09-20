import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.*;

public class ArrayDeque61BTest {

     @Test
     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
     void noNonTrivialFields() {
         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
     }

     @Test
     public void addFirstTestBasic() {
         Deque61B<String> ad1 = new ArrayDeque61B<>();
         ad1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(ad1.toList()).containsExactly("back").inOrder();

         ad1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(ad1.toList()).containsExactly("middle", "back").inOrder();

         ad1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(ad1.toList()).containsExactly("front", "middle", "back").inOrder();

         Deque61B<Integer> ad2 = new ArrayDeque61B<>();
         ad2.addLast(0);
         ad2.addLast(1);
         ad2.addFirst(-1);
         ad2.addLast(2);
         ad2.addFirst(-2);
         ad2.removeFirst();
         ad2.removeFirst();
         ad2.removeFirst();
         ad2.removeFirst();
         ad2.removeFirst();
         ad2.addFirst(2);
         assertThat(ad2.toList()).containsExactly(2).inOrder();
     }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque61B<String> ad1 = new ArrayDeque61B<>();

        ad1.addLast("front"); // after this call we expect: ["front"]
        ad1.addLast("middle"); // after this call we expect: ["front", "middle"]
        ad1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(ad1.toList()).containsExactly("front", "middle", "back").inOrder();

        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad2.addLast(0);
        ad2.addLast(1);
        ad2.addFirst(-1);
        ad2.addLast(2);
        ad2.addFirst(-2);
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.addLast(2);
        assertThat(ad2.toList()).containsExactly(2).inOrder();
    }

    @Test
    public void toListTest() {
        Deque61B<String> ad1 = new ArrayDeque61B<>();
        assertThat(ad1.toList()).containsExactly().inOrder();

    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();

        ad1.addLast(0);   // [0]
        ad1.addLast(1);   // [0, 1]
        ad1.addFirst(-1); // [-1, 0, 1]
        ad1.addLast(2);   // [-1, 0, 1, 2]
        ad1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(ad1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    @Test
    public void isEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        assertThat(ad1.isEmpty()).isTrue();

        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad2.addLast(3);   // [3]
        ad2.addLast(1);   // [3, 1]
        ad2.addFirst(-6); // [-6, 3, 1]
        ad2.addLast(0);   // [-6, 3, 1, 0]
        ad2.addFirst(-3); // [-3, -6, 3, 1, 0]
        assertThat(ad2.isEmpty()).isFalse();
    }

    @Test
    public void sizeTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();

        ad1.addLast(3);   // [3]
        ad1.addLast(1);   // [3, 1]
        ad1.addFirst(-6); // [-6, 3, 1]
        ad1.addLast(0);   // [-6, 3, 1, 0]
        ad1.addFirst(-3); // [-3, -6, 3, 1, 0]
        assertThat(ad1.size()).isEqualTo(5);

        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad2.addLast(3);   // [3]
        ad2.addLast(1);   // [3, 1]
        ad2.addFirst(-6); // [-6, 3, 1]
        ad2.addLast(0);   // [-6, 3, 1, 0]
        ad2.addFirst(-3); // [-3, -6, 3, 1, 0]
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        assertThat(ad2.size()).isEqualTo(0);

        Deque61B<Integer> ad3 = new ArrayDeque61B<>();
        ad2.removeFirst();
        ad2.removeFirst();
        assertThat(ad3.size()).isEqualTo(0);

    }

    @Test
    public void getTest() {
        Deque61B<String> ad1 = new ArrayDeque61B<>();

        ad1.addLast("Korai");
        ad1.addFirst("Tenma");
        ad1.addLast("Tooru");
        ad1.addFirst("Kenma");
        ad1.addFirst("Kei");
        ad1.addLast("Tobio"); // ["Kei", "Kenma", "Tenma", "Korai", "Tooru", "Tobio"]

        assertThat(ad1.get(-1)).isEqualTo(null);
        assertThat(ad1.get(2)).isEqualTo("Tenma");
        assertThat(ad1.get(10)).isEqualTo(null);
        assertThat(ad1.get(3)).isEqualTo("Korai");
        assertThat(ad1.get(0)).isEqualTo("Kei");
        assertThat(ad1.get(4)).isEqualTo("Tooru");
    }

    @Test
    public void removeFirstTest() {
        Deque61B<String> ad1 = new ArrayDeque61B<>();
        ad1.addLast("Korai");
        ad1.addFirst("Tenma");
        ad1.addLast("Tooru");
        ad1.addFirst("Kenma");
        ad1.addFirst("Kei");
        ad1.addLast("Tobio");
        ad1.removeFirst();
        ad1.removeFirst();
        assertThat(ad1.toList()).containsExactly("Tenma", "Korai", "Tooru", "Tobio").inOrder();

        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad2.addLast(0);
        ad2.addLast(1);
        ad2.addFirst(-1);
        ad2.addLast(2);
        ad2.addFirst(-2);
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        ad2.removeFirst();
        assertThat(ad2.toList()).containsExactly().inOrder();

    }

    @Test
    public void removeLastTest() {
        Deque61B<String> ad1 = new ArrayDeque61B<>();
        ad1.addLast("Korai");
        ad1.addFirst("Tenma");
        ad1.addLast("Tooru");
        ad1.addFirst("Kenma");
        ad1.addFirst("Kei");
        ad1.addLast("Tobio");
        ad1.removeLast();
        ad1.removeLast();
        assertThat(ad1.toList()).containsExactly("Kei", "Kenma", "Tenma", "Korai").inOrder();

        Deque61B<Integer> ad2 = new ArrayDeque61B<>();
        ad2.addLast(0);
        ad2.addLast(1);
        ad2.addFirst(-1);
        ad2.addLast(2);
        ad2.addFirst(-2);
        ad2.removeLast();
        ad2.removeLast();
        ad2.removeLast();
        ad2.removeLast();
        ad2.removeLast();
        assertThat(ad2.toList()).containsExactly().inOrder();

    }

    @Test
    public void resizeUpAndResizeDownTest() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();

        // Trigger resize up by adding more than the initial capacity
        int initialCapacity = 8;  // Assuming the initial capacity is 8
        for (int i = 0; i < initialCapacity; i++) {
            deque.addLast(i);
        }

        // At this point, the array should resize when adding one more element
        deque.addLast(initialCapacity);  // Should trigger a resize up

        assertEquals("Size after resizing up should be 9", 9, deque.size());
        for (int i = 0; i < 9; i++) {
            assertEquals("Elements should match after resizing up", Integer.valueOf(i), deque.get(i));
        }

        // Now trigger a resize down by removing enough elements
        for (int i = 0; i < 7; i++) {
            deque.removeFirst();
        }

        // At this point, after removing, the array should resize down
        assertEquals("Size after resizing down should be 2", 2, deque.size());

        // Ensure the remaining elements are still correct
        assertEquals("Element 0 should remain after resize down", Integer.valueOf(7), deque.get(0));
        assertEquals("Element 1 should remain after resize down", Integer.valueOf(8), deque.get(1));
    }

}
