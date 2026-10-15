package ch.heig.sio.lab2.groupG;

import ch.heig.sio.lab2.coloring.Coloring;
import ch.heig.sio.lab2.coloring.ColoringAlgorithm;
import ch.heig.sio.lab2.graph.Graph;

import java.util.Arrays;

/**
 * Exemple minimal : une couleur par sommet. Nul mais valide.
 */
public final class DSATURColoring implements ColoringAlgorithm {

    @Override
    public Coloring color(Graph graph) {

        int[] colors = new int[graph.vertexCount()];
        Arrays.fill(colors, -1);
        int[] satur = new int[graph.vertexCount()];
        boolean[][] forbidden = new boolean[graph.vertexCount()][graph.vertexCount() - 1];
        int coloredVertex = 0;
        int i = 0;
        for (int k = 1; k < graph.vertexCount(); k++){
            if (graph.degree(k) > graph.degree(i)){
                i = k;
            }
        }
        colors[i] = 0;
        coloredVertex++;
        for (int k :  graph.neighbors(i)){
            if (!forbidden[k][0]){
                forbidden[k][0] = true;
                satur[k]++;
            }
        }
        while (coloredVertex != graph.vertexCount()){
            int u = 0;
            for (int j = 0; j < graph.vertexCount(); j++){
                if (colors[j] == -1){
                    u = j;
                    break;
                }
            }

            for (int k = 0; k < graph.vertexCount(); k++) {
                if (colors[k] != -1) continue;
                //todo turn into function
                if (satur[k] > satur[u]){
                    u = k;
                } else if (satur[k] == satur[u]) {
                    if (graph.degree(k) > graph.degree(u)){
                        u = k;
                    }
                }
            }
            for (int j = 0; j < graph.vertexCount() - 1; j++){
                if (!forbidden[u][j]) {
                    colors[u] = j;
                    coloredVertex++;
                    break;
                }
            }
            for (int k :  graph.neighbors(u)){
                if (colors[k] == -1 && !forbidden[k][colors[u]]){
                    forbidden[k][colors[u]] = true;
                    satur[k]++;
                }
            }
        }

        return new Coloring(colors);
    }
}
