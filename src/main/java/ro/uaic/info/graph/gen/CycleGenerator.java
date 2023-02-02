/*
 * Copyright (C) 2022 Cristian Frăsinaru and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ro.uaic.info.graph.gen;

import ro.uaic.info.graph.Digraph;
import ro.uaic.info.graph.Graph;
import ro.uaic.info.graph.GraphBuilder;

/**
 *
 * @author Cristian Frăsinaru
 */
public class CycleGenerator extends AbstractGenerator {

    public CycleGenerator(int numVertices) {
        super(numVertices);
    }

    public CycleGenerator(int firstVertex, int lastVertex) {
        super(firstVertex, lastVertex);
    }

    /**
     *
     * @return
     */
    public Graph createGraph() {
        int n = vertices.length;
        var g = GraphBuilder.vertices(vertices)
                .estimatedAvgDegree(2)
                .named("C" + n)
                .buildGraph();
        addEdges(g, true);
        return g;
    }

    /**
     *
     * @param clockwise the orientation of the cycle
     * @return
     */
    public Digraph createDigraph(boolean clockwise) {
        var g = GraphBuilder.vertices(vertices)
                .estimatedAvgDegree(1)
                .buildDigraph();
        addEdges(g, clockwise);
        return g;
    }

    private void addEdges(Graph g, boolean clockwise) {
        for (int i = 0, n = vertices.length; i < n; i++) {
            int v = vertices[i];
            int u = vertices[(i + 1) % n];
            if (clockwise) {
                g.addEdge(v, u);
            } else {
                g.addEdge(u, v);
            }
        }
    }

}
