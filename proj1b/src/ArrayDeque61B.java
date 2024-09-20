import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class ArrayDeque61B<T> implements Deque61B<T> {
    private int size;
    private T[] items;
    private int first;
    private int last;


    public ArrayDeque61B() {
        items = (T[]) new Object[8];
        first = 0;
        last = 0;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        first = Math.floorMod(first - 1, items.length);
        items[first] = x;
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[last] = x;
        last = Math.floorMod(last + 1, items.length);
        size += 1;
    }

    @Override
    public List<T> toList() {
        T[] returnArray = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            returnArray[i] = items[Math.floorMod(first + i, items.length)];  // Handle circular nature
        }
        return List.of(returnArray);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size != 0) {
            T returnItem = items[first];
            items[first] = null;
            first = Math.floorMod(first + 1, items.length);
            size--;
            if (size > 0 && size <= items.length / 4) {
                resize(items.length / 2);
            }
            return returnItem;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size != 0) {
            last = Math.floorMod(last - 1, items.length);
            T returnItem = items[last];
            items[last] = null;
            size--;
            if (size > 0 && size <= items.length / 4) {
                resize(items.length / 2);
            }
            return returnItem;
        }
        return null;
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index < size) {
            return items[Math.floorMod(first + index, items.length)];
        }
        return null;
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[Math.floorMod(first + i, items.length)];
        }
        items = newItems;
        first = 0;
        last = size;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            T item = get(current);
            current++;
            return item;
        }
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i += 1) {
            if (items[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        // Check if the other object is an instance of Deque61B (common interface)
        if (other instanceof Deque61B<?>) {
            Deque61B<?> otherDeque = (Deque61B<?>) other;

            // Check if sizes are the same
            if (this.size() != otherDeque.size()) {
                return false;
            }

            // Compare each element, assuming both deques store elements in the same order
            for (int i = 0; i < this.size(); i++) {
                T thisElement = this.get(i);
                Object otherElement = otherDeque.get(i);
                // null safety
                if (thisElement == null ? otherElement != null : !thisElement.equals(otherElement)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("{");
        int i = 0;
        for (T item : this) {
            returnString.append(item.toString());
            if (i < size - 1) {
                returnString.append(", ");  // Only add ", " if it's not the last element
            }
            i++;
        }
        returnString.append("}");
        return returnString.toString();
    }
}
