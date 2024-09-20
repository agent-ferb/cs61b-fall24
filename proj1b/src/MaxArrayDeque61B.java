import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque61B(Comparator<T> c) {
        super();
        this.comparator = c;
    }

    // Returns the maximum element as per the comparator given in the constructor
    public T max() {
        if (isEmpty()) {
            return null;
        }

        T maxElement = get(0);
        for (int i = 1; i < size(); i++) {
            T currentElement = get(i);
            if (comparator.compare(currentElement, maxElement) > 0) {
                maxElement = currentElement;
            }
        }
        return maxElement;
    }

    // Returns the maximum element as per the given comparator c
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T maxElement = get(0);
        for (int i = 1; i < size(); i++) {
            T currentElement = get(i);
            if (c.compare(currentElement, maxElement) > 0) {
                maxElement = currentElement;
            }
        }
        return maxElement;
    }
}

