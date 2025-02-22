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
package org.graph4j.demo;

import org.graph4j.generate.GraphGenerator;

/**
 *
 * @author Cristian Frăsinaru
 */
class ContainsEdgeDemo extends PerformanceDemo {

    public ContainsEdgeDemo() {
        numVertices = 1000;
        runGuava = true;
        runJung = true;
        //runJGraphT = true;
        //runJGraphF= true;
    }

    @Override
    protected void createGraph() {
        graph = GraphGenerator.complete(numVertices);

    }

    @Override
    protected void testGraph4J() {
        int k = 0;
        for (int i = 0, n = graph.numVertices(); i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (graph.containsEdge(i, j)) {
                    k++;
                }
            }
        }
        System.out.println(k + " = " + graph.numEdges());
    }

    @Override
    protected void testJGraphT() {
        int k = 0;
        for (int i = 0, n = jgrapht.vertexSet().size(); i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (jgrapht.containsEdge(i, j)) {
                    k++;
                }
            }
        }
        System.out.println(k + " = " + jgrapht.edgeSet().size());
    }

    @Override
    protected void testJGraphF() {
        int k = 0;
        for (int i = 0, n = jgraphf.vertexSet().size(); i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (jgraphf.containsEdge(i, j)) {
                    k++;
                }
            }
        }
        System.out.println(k + " = " + jgraphf.edgeSet().size());
    }
    
    @Override
    protected void testGuava() {
        int k = 0;
        for (int i = 0, n = guavaGraph.nodes().size(); i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (guavaGraph.hasEdgeConnecting(i, j)) {
                    k++;
                }
            }
        }
        System.out.println(k + " = " + guavaGraph.edges().size());
    }

    @Override
    protected void testJung() {
        int k = 0;
        for (int i = 0, n = jungGraph.getVertexCount(); i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (jungGraph.isNeighbor(i, j)) {
                    k++;
                }
            }
        }
        System.out.println(k + " = " + jungGraph.getEdgeCount());
    }

    @Override
    protected void prepareArgs() {
        int steps = 10;
        args = new int[steps];
        for (int i = 0; i < steps; i++) {
            args[i] = 1000 * (i + 1);
        }
    }
}
