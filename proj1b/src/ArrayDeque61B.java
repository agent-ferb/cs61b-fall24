import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

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
        size ++;
    }

    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[last] = x;
        last = Math.floorMod(last + 1, items.length);
        size ++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            returnList.add(items[Math.floorMod(first + i, items.length)]);  // Handle circular nature
        }
        return List.copyOf(returnList);
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
}
