/**
 * Created by Yishuo on 2017/4/17.
 */
// A Java program for Bellman-Ford's single source shortest path
// algorithm.
import java.util.*;
import java.lang.*;
import java.io.*;

// A class to represent a connected, directed and weighted graph
class Graph
{
    // A class to represent a weighted edge in graph
    class Edge {
        String src, dest;
        int weight;
        Edge() {
            src = dest = "";
            weight = 0;
        }
    };

    int V;//V is the number of vertex;
    int E;//E is the number of edges
    Edge edge[];

    // Creates a graph with V vertices and E edges
    Graph(int v, int e)
    {
        V = v;
        E = e;
        edge = new Edge[e];
        for (int i=0; i<e; ++i) {
            edge[i] = new Edge();
        }
        edge[0].src = "H1"; edge[0].dest = "R1";edge[0].weight = Integer.MAX_VALUE;
        edge[1].src = "R1"; edge[1].dest = "R2";edge[1].weight = Integer.MAX_VALUE;
        edge[2].src = "R1"; edge[2].dest = "R3";edge[2].weight = Integer.MAX_VALUE;
        edge[3].src = "R2"; edge[3].dest = "R4";edge[3].weight = Integer.MAX_VALUE;
        edge[4].src = "R3"; edge[4].dest = "R4";edge[4].weight = Integer.MAX_VALUE;
        edge[5].src = "R4"; edge[5].dest = "H2";edge[5].weight = Integer.MAX_VALUE;
    }

    // The main function that finds shortest distances from src
    // to all other vertices using Bellman-Ford algorithm.  The
    // function also detects negative weight cycle
    //graph is the graph, src is the source point we will use for Bellman
    static HashMap<String, Integer> map;
    void BellmanFord(Graph graph, String src)
    {
        int V = graph.V, E = graph.E;
        int dist[] = new int[V];

        // Step 1: Initialize distances from src to all other
        // vertices as INFINITE
        for (int i=0; i<V; ++i)
            dist[i] = Integer.MAX_VALUE;
        dist[map.get(src)] = 0;

        // Step 2: Relax all edges |V| - 1 times. A simple
        // shortest path from src to any other vertex can
        // have at-most |V| - 1 edges
        for (int i=1; i<V; ++i)
        {
            for (int j=0; j<E; ++j)
            {
                int u = map.get(graph.edge[j].src);
                int v = map.get(graph.edge[j].dest);
                int weight = graph.edge[j].weight;
                if (dist[u] != Integer.MAX_VALUE &&
                        dist[u] + weight<dist[v])
                    dist[v] = dist[u] + weight;
            }
        }

        // Step 3: check for negative-weight cycles.  The above
        // step guarantees shortest distances if graph doesn't
        // contain negative weight cycle. If we get a shorter
        //  path, then there is a cycle.
        for (int j=0; j<E; ++j)
        {
            int u = map.get(graph.edge[j].src);
            int v = map.get(graph.edge[j].dest);
            int weight = graph.edge[j].weight;
            if (dist[u] != Integer.MAX_VALUE &&
                    dist[u] + weight < dist[v])
                System.out.println("Graph contains negative weight cycle");
        }
        printArr(dist, V, src);
    }

    // A utility function used to print the solution
    void printArr(int dist[], int V, String src)
    {
        try{
            File file = new File(src + "_re.txt");
            //if file doesnt exists, then create it
            if(!file.exists()){
                file.createNewFile();
            }

            //true = append file
            FileWriter fileWritter = new FileWriter(file.getName());
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write("Vertex        Distance from Source " + src + "\r\n");
            bufferWritter.newLine();

            //sort the HashMap for write txt
            List<Map.Entry<String, Integer>> infoIds =
                    new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (o1.getValue() - o2.getValue());
                }
            });

            for (int i = 0; i < infoIds.size(); i++) {
                bufferWritter.write(infoIds.get(i).getKey() + "        " + dist[i] + "\r\n");
                bufferWritter.newLine();
            }
            bufferWritter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    static void build_Graph(String Node, String Node2){
        try {
            File filename = new File(Node + Node2 + ".txt");
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                String[] eachLine = line.split(" ");
                if(eachLine[0].equals(Node) && eachLine[1].equals(Node2)){
                    File file = new File("beginning.txt");
                    //if file doesnt exists, then create it
                    if(!file.exists()){
                        file.createNewFile();
                    }

                    //true = append file
                    FileWriter fileWritter = new FileWriter(file.getName(), true);
                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                    bufferWritter.write(line + "\r\n");
                    // bufferWritter.newLine();
                    bufferWritter.close();
                    break;
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Driver method to test above function
    static void run_Bellman()
    {
        int V = 6;  // Number of vertices in graph
        int E = 6;  // Number of edges in graph
        map = new HashMap<>();
        map.put("H1", 0);
        map.put("R1", 1);
        map.put("R2", 2);
        map.put("R3", 3);
        map.put("R4", 4);
        map.put("H2", 5);

        Graph graph = new Graph(V, E);

        try {
            File filename = new File("beginning.txt");
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                String[] eachline = line.split(" ");
                String s = eachline[0];
                String d = eachline[1];
                String w = eachline[2];
                if(s.equals("H1"))
                    graph.edge[0].weight = Integer.parseInt(w);
                else if(s.equals("R1") && d.equals("R2"))
                    graph.edge[1].weight = Integer.parseInt(w);
                else if(s.equals("R1") && d.equals("R3"))
                    graph.edge[2].weight = Integer.parseInt(w);
                else if(s.equals("R2") && d.equals("R4"))
                    graph.edge[3].weight = Integer.parseInt(w);
                else if(s.equals("R3") && d.equals("R4"))
                    graph.edge[4].weight = Integer.parseInt(w);
                else
                    graph.edge[5].weight = Integer.parseInt(w);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        graph.BellmanFord(graph, "H1");
        graph.BellmanFord(graph, "H2");
        graph.BellmanFord(graph, "R1");
        graph.BellmanFord(graph, "R2");
        graph.BellmanFord(graph, "R3");
        graph.BellmanFord(graph, "R4");

    }
}
