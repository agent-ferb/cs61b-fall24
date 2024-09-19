import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

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
         Deque61B<String> lld1 = new ArrayDeque61B<>();
         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         Deque61B<Integer> lld2 = new ArrayDeque61B<>();
         lld2.addLast(0);
         lld2.addLast(1);
         lld2.addFirst(-1);
         lld2.addLast(2);
         lld2.addFirst(-2);
         lld2.removeFirst();
         lld2.removeFirst();
         lld2.removeFirst();
         lld2.removeFirst();
         lld2.removeFirst();
         lld2.addFirst(2);
         assertThat(lld2.toList()).containsExactly(2).inOrder();
     }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

        Deque61B<Integer> lld2 = new ArrayDeque61B<>();
        lld2.addLast(0);
        lld2.addLast(1);
        lld2.addFirst(-1);
        lld2.addLast(2);
        lld2.addFirst(-2);
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.addLast(2);
        assertThat(lld2.toList()).containsExactly(2).inOrder();
    }

    @Test
    public void toListTest() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.toList()).containsExactly().inOrder();

    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    @Test
    public void isEmptyTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.isEmpty()).isTrue();

        Deque61B<Integer> lld2 = new ArrayDeque61B<>();
        lld2.addLast(3);   // [3]
        lld2.addLast(1);   // [3, 1]
        lld2.addFirst(-6); // [-6, 3, 1]
        lld2.addLast(0);   // [-6, 3, 1, 0]
        lld2.addFirst(-3); // [-3, -6, 3, 1, 0]
        assertThat(lld2.isEmpty()).isFalse();
    }

    @Test
    public void sizeTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addLast(3);   // [3]
        lld1.addLast(1);   // [3, 1]
        lld1.addFirst(-6); // [-6, 3, 1]
        lld1.addLast(0);   // [-6, 3, 1, 0]
        lld1.addFirst(-3); // [-3, -6, 3, 1, 0]
        assertThat(lld1.size()).isEqualTo(5);

        Deque61B<Integer> lld2 = new ArrayDeque61B<>();
        lld2.addLast(3);   // [3]
        lld2.addLast(1);   // [3, 1]
        lld2.addFirst(-6); // [-6, 3, 1]
        lld2.addLast(0);   // [-6, 3, 1, 0]
        lld2.addFirst(-3); // [-3, -6, 3, 1, 0]
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        assertThat(lld2.size()).isEqualTo(0);

        Deque61B<Integer> lld3 = new ArrayDeque61B<>();
        lld2.removeFirst();
        lld2.removeFirst();
        assertThat(lld3.size()).isEqualTo(0);

    }

    @Test
    public void getTest() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();

        lld1.addLast("Korai");
        lld1.addFirst("Tenma");
        lld1.addLast("Tooru");
        lld1.addFirst("Kenma");
        lld1.addFirst("Kei");
        lld1.addLast("Tobio"); // ["Kei", "Kenma", "Tenma", "Korai", "Tooru", "Tobio"]

        assertThat(lld1.get(-1)).isEqualTo(null);
        assertThat(lld1.get(2)).isEqualTo("Tenma");
        assertThat(lld1.get(10)).isEqualTo(null);
        assertThat(lld1.get(3)).isEqualTo("Korai");
        assertThat(lld1.get(0)).isEqualTo("Kei");
        assertThat(lld1.get(4)).isEqualTo("Tooru");
    }

    @Test
    public void removeFirstTest() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();
        lld1.addLast("Korai");
        lld1.addFirst("Tenma");
        lld1.addLast("Tooru");
        lld1.addFirst("Kenma");
        lld1.addFirst("Kei");
        lld1.addLast("Tobio");
        lld1.removeFirst();
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly("Tenma", "Korai", "Tooru", "Tobio").inOrder();

        Deque61B<Integer> lld2 = new ArrayDeque61B<>();
        lld2.addLast(0);
        lld2.addLast(1);
        lld2.addFirst(-1);
        lld2.addLast(2);
        lld2.addFirst(-2);
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        assertThat(lld2.toList()).containsExactly().inOrder();

    }

    @Test
    public void removeLastTest() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();
        lld1.addLast("Korai");
        lld1.addFirst("Tenma");
        lld1.addLast("Tooru");
        lld1.addFirst("Kenma");
        lld1.addFirst("Kei");
        lld1.addLast("Tobio");
        lld1.removeLast();
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly("Kei", "Kenma", "Tenma", "Korai").inOrder();

        Deque61B<Integer> lld2 = new ArrayDeque61B<>();
        lld2.addLast(0);
        lld2.addLast(1);
        lld2.addFirst(-1);
        lld2.addLast(2);
        lld2.addFirst(-2);
        lld2.removeLast();
        lld2.removeLast();
        lld2.removeLast();
        lld2.removeLast();
        lld2.removeLast();
        assertThat(lld2.toList()).containsExactly().inOrder();

    }

}
