package deque;

import java.util.List;
import java.util.ArrayList;


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
            return (T)first;
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
            return (T)last;
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
}
