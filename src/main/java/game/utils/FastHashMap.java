package game.utils;


//no stinky standard library shenanigans here
//just good code
public class FastHashMap<K, V> {

    private class Node {
        public K key;
        public V value;
        public Node next;
        public Node prev;


        public Node(K key, V value) { this.key = key; this.value = value; }
        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value; this.next = next;
            if (next != null) next.prev = this;
        }
    }

    Node[] table;


    public FastHashMap() {
        table = new FastHashMap.Node[100];
    }


    public FastHashMap(int numbuckets) {
        table = new FastHashMap.Node[numbuckets];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }


    public void insert(K key, V value) {
        int hash = hash(key);

        table[hash] = new Node(key, value, table[hash]);
    }

    public V get(K key) {
        int hash = hash(key);

        Node n = table[hash];

        while (n != null) {
            if (n.key.equals(key)) {
                return n.value;
            }

            n = n.next;
        }

        return null;
    }


    public V get(K key, V or_insert) {
        var node = get(key);
        if (node == null) {
            insert(key, or_insert);
            return or_insert;
        }

        return node;
    }

    public V remove(K key) {
        int hash = hash(key);

        Node n = table[hash];

        while (n != null) {
            if (n.key.equals(key)) {

                if (n.prev == null) {
                    table[hash] = n.next;
                } else {
                    n.prev.next = n.next;
                }

                if (n.next != null) {
                    n.next.prev = n.prev;
                }

                return n.value;
            }

            n = n.next;
        }

        return null;
    }
}
