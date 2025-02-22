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

import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.graph4j.generate.RandomGnpGraphGenerator;
import org.graph4j.util.Tools;

/**
 *
 * @author Cristian Frăsinaru
 */
@Deprecated
class RandomGraphDemo extends PerformanceDemo {

    private int n = 10_000;
    private double p = 0.05;

    public RandomGraphDemo() {
        runJGraphT = true;
    }

    @Override
    protected void testGraph4J() {
        var g = new RandomGnpGraphGenerator(n, p).createGraph();
    }

    @Override
    protected void testJGraphT() {
        var gnp = new GnpRandomGraphGenerator<Integer, DefaultEdge>(n, p);
        gnp.generateGraph(Converter.createJGraphT(null));
    }
    
    public static void main(String args[]) {
        var app = new RandomGraphDemo();
        app.demo();
    }

}
