package ch.heig.sio.lab2.groupG;

import ch.heig.sio.lab2.coloring.Coloring;
import ch.heig.sio.lab2.coloring.ColoringAlgorithm;
import ch.heig.sio.lab2.graph.Graph;
import ch.heig.sio.lab2.graph.GraphReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Main {

  public static void main(String[] args) throws IOException {
    Path dataDir = Path.of(args.length > 0 ? args[0] : "data");

    //ColoringAlgorithm algorithm = new TrivialColoring();
    ArrayList<ColoringAlgorithm> algorithms = new ArrayList<>();
    algorithms.add(new DSATURColoring());
    algorithms.add(new LargestFirstColoring());

    List<Path> files;
    try (Stream<Path> stream = Files.list(dataDir)) {
      files = stream.filter(p -> p.toString().endsWith(".txt")).sorted().toList();
    }

    for (Path file : files) {
      Graph graph;
      try {
        graph = GraphReader.read(file);
      } catch (IOException e) {
        System.err.println(file.getFileName() + " : " + e.getMessage());
        continue;
      }
      System.out.println(file.getFileName().toString().split("\\.")[0] + " " + getInfo(graph));
      long[] times = new long[2];
      int[] colors = new int[2];
      for (int i = 0 ; i < algorithms.toArray().length; i++){
        long start = System.nanoTime();
        Coloring coloring = algorithms.get(i).color(graph);
        times[i] = (System.nanoTime() - start) / 1_000;
        colors[i] = coloring.colorCount();
        System.out.printf("  %-16s %5d couleurs  %8d µs  %s%n",
            algorithms.get(i).name(), colors[i], times[i],
            coloring.isProperFor(graph) ? "OK" : "INVALIDE");

      }
      System.out.printf("  Gain relatif de DSATUR sur LF sur les couleurs      : %f%% \n", (double)(colors[1]-colors[0])/colors[1] * 100) ;
      System.out.printf("  Augmentation relative entre DSATUR sur le temps     : %f%% \n\n", (double)(times[0]-times[1])/times[1] * 100);
    }
  }
  private static String getInfo(Graph g){
    int n = g.vertexCount();
    int m =  g.edgeCount();
    double density = 0;
    if (n >= 2) {
      density = (2 * (double)m) / (n * (n-1));
    }
    return "[n=" + n + ", m=" + m + ", ∆=" + g.maxDegree() + ", D=" + density + "]";
  }
}
