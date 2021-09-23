import java.util.*;

public class MyLinkedHashMap<K, V> {

    public class Node {
        K key;
        V value;
        Node next;
        Node prev;

        Node(K key, V value, Node prev, Node next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private final HashMap<K, Node> map;
    private Node head;
    private Node tail;
    public final int MAX_SIZE = 1_000_000;

    public MyLinkedHashMap() {
        this.map = new HashMap<>();
        head = tail = null;
    }

    public MyLinkedHashMap(MyLinkedHashMap<K, V> map) {
        this.map = new HashMap<>(map.map);
        head = map.head;
        tail = map.tail;
    }

    public void put(K key, V value) {
        assert size() <= MAX_SIZE;
        boolean containsKeyOrMaxSize = containsKey(key) || size() == MAX_SIZE;
        int oldSize = size();
        if (containsKey(key)) {
            remove(key);
        } else {
            if (size() == MAX_SIZE) {
                remove(head.key);
            }
        }
        Node node = new Node(key, value, tail, null);

        if (tail != null) {
            tail.next = node;
        } else {
            head = node;
        }
        tail = node;
        map.put(key, node);
        assert (containsKeyOrMaxSize ? oldSize == size() : oldSize + 1 == size()) && node == tail;
    }

    public V get(K key) {
        int oldSize = size();
        V value = null;
        if (containsKey(key)) {
            moveToLast(map.get(key));
            value = map.get(key).value;
        }
        assert oldSize == size();
        return value;
    }

    public int size() {
        int size = map.size();
        assert 0 <= size && size <= MAX_SIZE;
        return size;
    }

    public void clear(){
        map.clear();
        head = tail = null;
        assert size() == 0;
    }

    public void remove(K key) {
        assert containsKey(key) ? size() > 0 : size() >= 0;
        boolean containsKey = containsKey(key);
        int oldSize = size();
        if (containsKey(key)) {
            Node node = map.get(key);
            map.remove(key);
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node == tail) {
                tail = node.prev;
            }
            if (node == head) {
                head = node.next;
            }
        }
        assert containsKey ? oldSize == size() + 1 : oldSize == size();
    }

    public V getLastValue() {
        return getNode(tail);
    }

    public V getFirstValue() {
        return getNode(head);
    }

    private V getNode(Node node) {
        int oldSize = size();
        V value = null;
        if (node != null) {
            moveToLast(node);
            value = node.value;
        }
        assert oldSize == size();
        return value;
    }

    public void removeLastValue() {
        Node oldTail = tail;
        removeNode(tail);
        assert tail != oldTail;
    }

    public void removeFirstValue() {
        Node oldHead = head;
        removeNode(head);
        assert head != oldHead;
    }

    private void removeNode(Node node) {
        assert size() > 0 : "Can only be removed from non-empty map";
        int oldSize = size();
        remove(node.key);
        assert size() + 1 == oldSize;
    }

    private boolean containsKey(K key) {
        return map.containsKey(key);
    }

    private void moveToLast(Node node) {
        remove(node.key);
        put(node.key, node.value);
    }

}
