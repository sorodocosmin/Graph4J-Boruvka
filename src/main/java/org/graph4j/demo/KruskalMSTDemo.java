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

import edu.princeton.cs.algs4.KruskalMST;
import org.graph4j.alg.mst.KruskalMinimumSpanningTree;
import org.graph4j.alg.mst.ParallelFilterKruskal;
import org.graph4j.generate.EdgeWeightsGenerator;
import org.graph4j.generate.RandomGnpGraphGenerator;

/**
 *
 * @author Cristian Frăsinaru
 */
class KruskalMSTDemo extends PerformanceDemo {

    private final double probability = 0.1;

    public KruskalMSTDemo() {
        numVertices = 1000;
        //runJGraphT = true;
        runAlgs4 = true;
        runOther = true;
    }

    @Override
    protected void createGraph() {
        graph = new RandomGnpGraphGenerator(numVertices, probability).createGraph();
        EdgeWeightsGenerator.randomDoubles(graph, 0, 1);
    }

    @Override
    protected void testGraph4J() {
        var alg = new KruskalMinimumSpanningTree(graph);
        System.out.println(alg.getWeight());
        alg.getEdges();
    }

    @Override
    protected void testJGraphT() {
        var alg = new org.jgrapht.alg.spanning.KruskalMinimumSpanningTree(jgrapht);
        System.out.println(alg.getSpanningTree().getWeight());
    }

    @Override
    protected void testAlgs4() {
        var alg = new KruskalMST(algs4Ewg);
        System.out.println(alg.weight());
        alg.edges();
    }

    @Override
    protected void testOther() {
        var alg = new ParallelFilterKruskal(graph);
        System.out.println(alg.getWeight());
        alg.getEdges();
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
