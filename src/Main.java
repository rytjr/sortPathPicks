import java.io.*;
import java.util.*;

public class Main {
    static class Node implements Comparable<Node> {
        int vertex, weight;

        public Node(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.weight, o.weight);
        }
    }

    static int N, M, T, S, G, H;
    static List<Node>[] graph;
    static int[] destinations;
    static int INF = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int testCases = Integer.parseInt(bf.readLine());
        for (int t = 0; t < testCases; t++) {
            String[] firstLine = bf.readLine().split(" ");
            N = Integer.parseInt(firstLine[0]);
            M = Integer.parseInt(firstLine[1]);
            T = Integer.parseInt(firstLine[2]);

            String[] secondLine = bf.readLine().split(" ");
            S = Integer.parseInt(secondLine[0]);
            G = Integer.parseInt(secondLine[1]);
            H = Integer.parseInt(secondLine[2]);

            graph = new ArrayList[N + 1];
            for (int i = 0; i <= N; i++) graph[i] = new ArrayList<>();

            for (int i = 0; i < M; i++) {
                String[] edge = bf.readLine().split(" ");
                int a = Integer.parseInt(edge[0]);
                int b = Integer.parseInt(edge[1]);
                int d = Integer.parseInt(edge[2]);
                graph[a].add(new Node(b, d));
                graph[b].add(new Node(a, d));
            }

            destinations = new int[T];
            for (int i = 0; i < T; i++) {
                destinations[i] = Integer.parseInt(bf.readLine());
            }

            int[] distS = dijkstra(S); // S에서 모든 노드까지의 최단 거리
            int[] distG = dijkstra(G); // G에서 모든 노드까지의 최단 거리
            int[] distH = dijkstra(H); // H에서 모든 노드까지의 최단 거리

            List<Integer> results = new ArrayList<>();
            for (int destination : destinations) {
                int direct = distS[destination]; // S -> 목적지
                int viaGtoH = distS[G] + distG[H] + distH[destination]; // S -> G -> H -> 목적지
                int viaHtoG = distS[H] + distH[G] + distG[destination]; // S -> H -> G -> 목적지

                // 최단 경로가 반드시 G-H를 포함하는 경우
                if (direct == viaGtoH || direct == viaHtoG) {
                    results.add(destination);
                }
            }

            Collections.sort(results);
            for (int result : results) sb.append(result).append(" ");
            sb.append("\n");
        }

        System.out.print(sb);
    }

    // 다익스트라 알고리즘 구현
    static int[] dijkstra(int start) {
        int[] dist = new int[N + 1];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int currentVertex = current.vertex;
            int currentWeight = current.weight;

            if (dist[currentVertex] < currentWeight) continue;

            for (Node neighbor : graph[currentVertex]) {
                int nextVertex = neighbor.vertex;
                int weight = neighbor.weight + currentWeight;

                if (dist[nextVertex] > weight) {
                    dist[nextVertex] = weight;
                    pq.offer(new Node(nextVertex, weight));
                }
            }
        }
        return dist;
    }
}
