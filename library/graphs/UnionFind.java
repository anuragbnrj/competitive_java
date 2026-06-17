package library.graphs;

import java.util.Arrays;

public class UnionFind {
    private final int[] parent;
    private final int[] size;
    private int components;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        Arrays.fill(size, 1);
        components = n;
    }

    // Path-compressed find (iterative, no recursion-depth risk).
    public int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    // Union by size. Returns false if x and y were already connected
    // (useful for "does this edge create a cycle?").
    public boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;

        if (size[rx] < size[ry]) {
            int tmp = rx;
            rx = ry;
            ry = tmp;
        }
        parent[ry] = rx;
        size[rx] += size[ry];
        components--;
        return true;
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public int componentCount() {
        return components;
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(5);
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(3, 4);

        System.out.println("0 and 2 connected (expect true): " + uf.connected(0, 2));
        System.out.println("0 and 3 connected (expect false): " + uf.connected(0, 3));
        System.out.println("Components (expect 2): " + uf.componentCount());

        uf.union(2, 3);
        System.out.println("0 and 4 connected after merge (expect true): " + uf.connected(0, 4));
        System.out.println("Components (expect 1): " + uf.componentCount());
    }
}
