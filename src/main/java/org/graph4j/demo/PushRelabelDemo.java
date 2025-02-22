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

import org.graph4j.alg.flow.PushRelabelMaximumFlow;
import org.graph4j.alg.flow.EdmondsKarpMaximumFlow;
import org.jgrapht.alg.flow.PushRelabelMFImpl;
import org.graph4j.Digraph;
import org.graph4j.generate.EdgeWeightsGenerator;
import org.graph4j.generate.RandomGnpGraphGenerator;

/**
 *
 * @author Cristian Frăsinaru
 */
class PushRelabelDemo extends PerformanceDemo {

    private final double probability = 0.5;

    public PushRelabelDemo() {
        numVertices = 2000;
        runJGraphT = true;
        runAlgs4 = true;
    }

    @Override
    protected void createGraph() {
        graph = new RandomGnpGraphGenerator(numVertices, probability).createDigraph();
        //graph = new GnmRandomGenerator(numVertices, 3*numVertices).createDigraph();
        //graph = new CompleteGenerator(numVertices).createDigraph();
        //EdgeWeightsGenerator.randomIntegers(graph, 0, numVertices - 1);
        EdgeWeightsGenerator.randomDoubles(graph, 0, 1);
    }

    @Override
    protected void testGraph4J() {
        //((GraphImpl)graph).enableAdjListMatrix(); //not worth it
        var alg = new PushRelabelMaximumFlow((Digraph) graph, 0, numVertices - 1);
        //var alg = new FordFulkersonMaximumFlow((Digraph) graph, 0, numVertices - 1);
        System.out.println(alg.getValue());
    }

    @Override
    protected void testJGraphT() {
        var alg = new PushRelabelMFImpl(jgrapht);
        System.out.println(alg.calculateMaximumFlow(0, numVertices - 1));
    }

    @Override
    protected void testAlgs4() {
        //var alg = new FordFulkerson(algs4Net, 0, numVertices - 1);
        //System.out.println(alg.value());
        var alg = new EdmondsKarpMaximumFlow((Digraph) graph, 0, numVertices - 1);
        System.out.println(alg.getValue());
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
