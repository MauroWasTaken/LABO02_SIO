package ch.heig.sio.lab2.groupG;

import ch.heig.sio.lab2.coloring.Coloring;
import ch.heig.sio.lab2.coloring.ColoringAlgorithm;
import ch.heig.sio.lab2.graph.Graph;

import java.util.Arrays;

public final class LargestFirstColoring implements ColoringAlgorithm {

    @Override
    public Coloring color(Graph graph) {
        int n = graph.vertexCount();
        int[] colors = new int[n];

        Arrays.fill(colors, -1);
        int[] sortedIndices = countingSort(graph);

        for (int i = 0; i < n; i++) {
            int vertex = sortedIndices[i];
            boolean[] usedColors = new boolean[n];

            for (int j = 0; j < graph.degree(vertex); j++) {
                int neighbor = graph.neighbor(vertex, j);
                if (colors[neighbor] != -1) {
                    usedColors[colors[neighbor]] = true;
                }
            }

            int color;
            for (color = 0; color < n; color++) {
                if (!usedColors[color]) {
                    break;
                }
            }
            colors[vertex] = color;
        }

        return new Coloring(colors);
    }

    /**
     * O(N + M)
     * Tri par comptage des indices des sommets en fonction de leur degré.
     *
     * @param graph Le graphe dont on veut trier les sommets.
     * @return Les indices des sommets triés par degré décroissant.
     */
    private int[] countingSort(Graph graph) {
        int maxDegree = 0;
        int[] countingTab;
        int[] sortedTab = new int[graph.vertexCount()];

        for (int i = 0; i < graph.vertexCount(); i++) {
            maxDegree = Math.max(maxDegree, graph.degree(i));
        }

        countingTab = new int[maxDegree + 1];
        for (int vertex = 0; vertex < graph.vertexCount(); vertex++) {
            countingTab[graph.degree(vertex)]++;
        }

        int position = 0;
        int[] nextPosition = new int[maxDegree + 1];
        for (int degree = maxDegree; degree >= 0; degree--) {
            nextPosition[degree] = position;
            position += countingTab[degree];
        }

        for (int vertex = 0; vertex < graph.vertexCount(); vertex++) {
            int degree = graph.degree(vertex);
            sortedTab[nextPosition[degree]++] = vertex;
        }

        return sortedTab;
    }
}