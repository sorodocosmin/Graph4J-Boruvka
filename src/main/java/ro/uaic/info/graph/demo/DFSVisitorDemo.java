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
package ro.uaic.info.graph.demo;

import com.google.common.graph.Traverser;
import org.jgrapht.traverse.DepthFirstIterator;
import ro.uaic.info.graph.gen.GnpRandomGenerator;
import ro.uaic.info.graph.search.DFSIterator;
import ro.uaic.info.graph.search.DFSVisitor;
import ro.uaic.info.graph.search.DepthFirstSearch;
import ro.uaic.info.graph.search.SearchNode;

/**
 *
 * @author Cristian Frăsinaru
 */
public class DFSVisitorDemo extends PerformanceDemo {

    public DFSVisitorDemo() {
        runGuava = true;
    }

    @Override
    protected void createGraph() {
        graph = new GnpRandomGenerator(1000, 0.2).createGraph();
    }

    private int k1=0;
    @Override
    protected void testGraph4J() {
        for (int v : graph.vertices()) {
            new DepthFirstSearch(graph).traverse(new DFSVisitor(){
                @Override
                public void startVertex(SearchNode node) {
                    k1++;
                }
            },v);
        }
        System.out.println(k1);
    }

    
    private int k2 = 0;

    @Override
    protected void testGuava() {
        for (var v : guavaGraph.nodes()) {
            Traverser.forGraph(guavaGraph).depthFirstPostOrder(v)
                    .forEach(x -> k2++);
        }
        System.out.println(k2);
    }

    public static void main(String args[]) {
        var app = new DFSVisitorDemo();
        app.demo();
    }
}
