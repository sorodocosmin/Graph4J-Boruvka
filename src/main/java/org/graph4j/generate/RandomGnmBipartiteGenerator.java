/*
 * Copyright (C) 2023 Cristian Frăsinaru and contributors
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
package org.graph4j.generate;

import java.util.Random;
import org.graph4j.Digraph;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.util.CheckArguments;

/**
 * Random bipartite graph generator using Erdős–Rényi G(n,m) model.
 *
 * @author Cristian Frăsinaru
 */
public class RandomGnmBipartiteGenerator extends AbstractBipartiteGenerator {

    private final long numEdges;
    private final Random random = new Random();

    public RandomGnmBipartiteGenerator(int n1, int n2, long numEdges) {
        super(n1, n2);
        CheckArguments.numberOfEdges(numEdges);
        this.numEdges = numEdges;
    }

    public RandomGnmBipartiteGenerator(int first1, int last1, int first2, int last2, long numEdges) {
        super(first1, last1, first2, last2);
        CheckArguments.numberOfEdges(numEdges);
        this.numEdges = numEdges;
    }

    /**
     * 
     * @return a random bipartite directed graph.
     */
    public Digraph createDigraph() {
        int n1 = last1 - first1 + 1;
        int n2 = last2 - first2 + 1;
        var g = GraphBuilder.vertices(vertices)
                .estimatedAvgDegree(Math.max(n1, n2)).buildDigraph();
        addEdges(g, null);
        return g;
    }

    @Override
    protected void addEdges(Graph g, Boolean leftToRight) {
        checkMaxEdges(g);
        boolean safeMode = g.isSafeMode();
        g.setSafeMode(false);
        while (g.numEdges() < numEdges) {
            int v = first1 + random.nextInt(last1 - first1 + 1);
            int u = first2 + random.nextInt(last2 - first2 + 1);
            if (u == v) {
                continue;
            }
            boolean l2r;
            if (leftToRight != null) {
                l2r = leftToRight;
            } else {
                l2r = random.nextDouble() < 0.5;
            }
            if (l2r) {
                if (!g.containsEdge(v, u)) {
                    g.addEdge(v, u);
                }
            } else {
                if (!g.containsEdge(u, v)) {
                    g.addEdge(u, v);
                }
            }
        }
        g.setSafeMode(safeMode);
    }

    private void checkMaxEdges(Graph g) {
        long n1 = last1 - first1 + 1;
        long n2 = last2 - first2 + 1;
        long maxEdges = g.isDirected() ? 2 * n1 * n2 : n1 * n2;
        if (numEdges > maxEdges) {
            throw new IllegalArgumentException(
                    "The number of edges is greater than the maximum possible: "
                    + numEdges + " > " + maxEdges);
        }
    }

}
