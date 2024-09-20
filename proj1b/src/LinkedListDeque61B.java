import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LinkedListDeque61B<T> implements Deque61B<T> {
    private Node sentinel;
    private int size;

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node node = new Node(x, sentinel, sentinel.next);
        sentinel.next.prev = node;
        sentinel.next = node;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node node = new Node(x, sentinel.prev, sentinel);
        sentinel.prev.next = node;
        sentinel.prev = node;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node curr = sentinel.next;

        while (curr != sentinel) {
            returnList.add(curr.item);
            curr = curr.next;
        }
        return List.of(returnList.toArray((T[]) new Object[0]));
    }

    @Override
    public boolean isEmpty() {
        if (sentinel.next == sentinel && sentinel.prev == sentinel) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size != 0) {
            Node first = sentinel.next;
            sentinel.next = first.next;
            first.next.prev = sentinel;
            size--;
            return first.item;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size != 0) {
            Node last = sentinel.prev;
            sentinel.prev = last.prev;
            last.prev.next = sentinel;
            size--;
            return last.item;
        }
        return null;
    }

    @Override
    public T get(int index) {
        Node p = sentinel.next;
        int i = 0;
        while (index >= 0 && index < size) {
            while (i < index) {
                p = p.next;
                i++;
            }
            return p.item;
        }
        return null;
    }

    @Override
    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.next, index);
    }

    // Helper method that takes both the current node and the remaining index
    private T getRecursiveHelper(Node currentNode, int index) {
        if (index == 0) {
            return currentNode.item;
        } else if (currentNode == null || index < 0 || index >= size) {
            return null;
        } else {
            return getRecursiveHelper(currentNode.next, index - 1);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node current = sentinel.next;

        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public T next() {
            T item = current.item;
            current = current.next;
            return item;
        }
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i += 1) {
            if (get(i).equals(x)) {
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

                // Handle null elements safely
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
