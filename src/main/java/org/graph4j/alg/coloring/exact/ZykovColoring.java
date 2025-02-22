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
package org.graph4j.alg.coloring.exact;

import java.util.ArrayDeque;
import java.util.Deque;
import org.graph4j.Graph;
import org.graph4j.alg.clique.MaximalCliqueFinder;
import org.graph4j.alg.coloring.RecursiveLargestFirstColoring;
import org.graph4j.alg.coloring.VertexColoring;

/**
 * Useless.
 *
 * @author Cristian Frăsinaru
 */
public class ZykovColoring extends ExactColoringBase {

    private Deque<Node> stack;
    private int chi;

    public ZykovColoring(Graph graph) {
        super(graph);
    }

    public ZykovColoring(Graph graph, long timeLimit) {
        super(graph, timeLimit);
    }

    @Override
    protected ZykovColoring getInstance(Graph graph, long timeLimit) {
        return new ZykovColoring(graph, timeLimit);
    }

    @Override
    public VertexColoring findColoring() {
        compute();
        System.out.println(chi);
        return null;
    }

    @Override
    public VertexColoring findColoring(int numColors) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void compute() {
        chi = Integer.MAX_VALUE;
        stack = new ArrayDeque<>();
        stack.push(new Node(graph, initialColoring));
        while (!stack.isEmpty()) {
            var node = stack.pop();
            System.out.println(node.graph.numVertices() + ", " + node.graph.numEdges() + ", chi=" + chi + ", stack: " + stack.size());
            if (node.graph.isComplete()) {
                if (chi > node.graph.numVertices()) {
                    chi = node.graph.numVertices();
                }
                continue;
            }
            /*
            if (node.graph.numVertices() < 90) {
                System.out.println("Backtrack coloring");
                var bt = new ParallelBacktrackColoring(node.graph);
                var col = bt.findColoring();
                if (chi > col.numUsedColors()) {
                    chi = col.numUsedColors();
                }
                continue;
            }*/
            var clique = new MaximalCliqueFinder(node.graph).getMaximalClique();
            if (clique.numVertices() >= chi) {
                continue;
            }
            var col = new RecursiveLargestFirstColoring(node.graph).findColoring();
            if (chi > col.numUsedColors()) {
                chi = col.numUsedColors();
                if (chi == clique.numVertices()) {
                    continue;
                }
            }
            //choose a pair of vertices that are not adjacent, either:
            //a) one of them is colored and the other's domain contains 2 values
            // and the other is adjacent with as many vertices having 2 values in their domains
            //b) both have the same domain containing 2 values
            // and they are adjacent with an uncolored vertex 
            // which is adjacent with as many vertices having 2 values in their domains
            int n = node.graph.numVertices();
            int maxv = -1, maxu = -1, max = -1;
            /*
            for (int v : node.vertices()) {
                if (node.degree(v) == n - 1) {
                    continue;
                }
                for (int u : node.vertices()) {
                    if (u == v || node.containsEdge(v, u)) {
                        continue;
                    }
                    int d = node.degree(v) + node.degree(u);
                    if (max < d) {
                        max = d;
                        maxv = v;
                        maxu = u;
                    }
                }
            }*/
            var g1 = node.graph.copy();
            g1.addEdge(maxv, maxu);
            var col1 = new VertexColoring(g1, node.coloring);
            stack.push(new Node(g1, col1));

            var g2 = node.graph.copy();
            g2.contractVertices(maxv, maxu);
            var col2 = new VertexColoring(g2, node.coloring);
            stack.push(new Node(g2, col2));

        }
    }

    private class Node {

        Graph graph;
        VertexColoring coloring;

        public Node(Graph graph, VertexColoring coloring) {
            this.graph = graph;
            this.coloring = coloring;
        }

    }
}
