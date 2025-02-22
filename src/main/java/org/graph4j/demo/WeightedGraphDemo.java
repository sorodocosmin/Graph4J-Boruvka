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

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultGraphType;
import org.jgrapht.util.SupplierUtil;
import org.graph4j.Edge;
import org.graph4j.GraphBuilder;

/**
 *
 * @author Cristian Frăsinaru
 */
class WeightedGraphDemo extends PerformanceDemo {

    public WeightedGraphDemo() {
        numVertices = 5_000;
        runGuava = true;
        runJung = true;
        runJGraphT = true;
        //runJGraphF = true;
        //runAlgs4 = true;
    }

    @Override
    protected void testGraph4J() {
        var g = GraphBuilder.numVertices(numVertices)
                .estimatedAvgDegree(numVertices - 1)
                .buildGraph();
        for (int v = 0; v < numVertices - 1; v++) {
            for (int u = v + 1; u < numVertices; u++) {
                g.addEdge(v, u, 1);
            }
        }
    }

    @Override
    protected void testJGraphT() {
        var g = new org.jgrapht.graph.SimpleWeightedGraph<Integer, DefaultEdge>(DefaultEdge.class);
        for (int v = 0; v < numVertices; v++) {
            g.addVertex(v);
        }
        for (int v = 0; v < numVertices - 1; v++) {
            for (int u = v + 1; u < numVertices; u++) {
                var e = g.addEdge(v, u);
                g.setEdgeWeight(e, 1);
            }
        }
    }

    @Override
    protected void testJGraphF() {
        var g = new org.jgrapht.opt.graph.fastutil.FastutilMapIntVertexGraph(SupplierUtil.createIntegerSupplier(),
                SupplierUtil.createDefaultEdgeSupplier(), DefaultGraphType.simple().asWeighted(), false);
        //var jg = new org.jgrapht.opt.graph.fastutil.FastutilMapIntVertexGraph(null, null, DefaultGraphType.simple(), false);
        for (int v = 0; v < numVertices; v++) {
            g.addVertex(v);
        }
        for (int v = 0; v < numVertices - 1; v++) {
            for (int u = v + 1; u < numVertices; u++) {
                var e = g.addEdge(v, u);
                g.setEdgeWeight(e, 1);
            }
        }
    }

    @Override
    protected void testAlgs4() {
        var g = new edu.princeton.cs.algs4.EdgeWeightedGraph(numVertices);
        for (int v = 0; v < numVertices - 1; v++) {
            for (int u = v + 1; u < numVertices; u++) {
                g.addEdge(new edu.princeton.cs.algs4.Edge(v, u, 1));
            }
        }
    }

    @Override
    protected void testGuava() {
        var g = com.google.common.graph.ValueGraphBuilder.undirected()
                .expectedNodeCount(numVertices)
                .build();
        for (int v = 0; v < numVertices; v++) {
            g.addNode(v);
        }
        for (int v = 0; v < numVertices - 1; v++) {
            for (int u = v + 1; u < numVertices; u++) {
                g.putEdgeValue(u, v, 1);
            }
        }
    }

    @Override
    protected void testJung() {
        var g = new edu.uci.ics.jung.graph.UndirectedSparseGraph<Integer, Object>();
        for (int v = 0; v < numVertices; v++) {
            g.addVertex(v);
        }
        for (int v = 0; v < numVertices - 1; v++) {
            for (int u = v + 1; u < numVertices; u++) {
                g.addEdge(new Edge(v, u, 1), v, u);
            }
        }
    }

    @Override
    protected void prepareArgs() {
        int steps = 10;
        args = new int[steps];
        for (int i = 0; i < steps; i++) {
            args[i] = 500 * (i + 1);
        }
    }

}
