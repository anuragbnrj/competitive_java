package library;

class Node {
    private Node[] links;
    private int countEndsWith;
    private int countPrefix;
    private int minCostEndAt;
    
    public Node() {
        links = new Node[26];
        countEndsWith = 0;
        countPrefix = 0;
        minCostEndAt = (int)1E9;
    }

    public boolean containsKey(char ch) {
        return links[ch - 'a'] != null;
    }

    public Node get(char ch) {
        return links[ch - 'a'];
    }

    public void put(char ch, Node node) {
        links[ch - 'a'] = node;
    }

    public void increaseEnd(int cost) {
        countEndsWith++;
        minCostEndAt = Math.min(minCostEndAt, cost);
    }

    public void increasePrefix() {
        countPrefix++;
    }

    public void reduceEnd() {
        countEndsWith--;
    }

    public void reducePrefix() {
        countPrefix--;
    }

    public int getEndsWith() {
        return countEndsWith;
    }

    public int getPrefix() {
        return countPrefix;
    }
}

public class Trie {
    private Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String word, int cost) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (!node.containsKey(word.charAt(i))) {
                node.put(word.charAt(i), new Node());
            }
            node = node.get(word.charAt(i));
            node.increasePrefix();
        }
        node.increaseEnd(cost);
    }

    public int countWordsEqualTo(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (node.containsKey(word.charAt(i))) {
                node = node.get(word.charAt(i));
            } else {
                return 0;
            }
        }
        return node.getEndsWith();
    }

    public int countWordsStartingWith(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (node.containsKey(word.charAt(i))) {
                node = node.get(word.charAt(i));
            } else {
                return 0;
            }
        }
        return node.getPrefix();
    }

    public void erase(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (node.containsKey(word.charAt(i))) {
                node = node.get(word.charAt(i));
                node.reducePrefix();
            } else {
                return;
            }
        }
        node.reduceEnd();
    }
}