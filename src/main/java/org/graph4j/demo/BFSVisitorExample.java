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

import org.graph4j.GraphBuilder;
import org.graph4j.traverse.BFSTraverser;
import org.graph4j.traverse.SearchNode;
import org.graph4j.traverse.BFSVisitor;

/**
 *
 * @author Cristian Frăsinaru
 */
class BFSVisitorExample {

    private void demoBFS() {
        /*
        var g = GraphBuilder
                .vertexRange(1, 8)
                .addPath(1, 2, 4, 6).addEdge(6, 2)
                .addPath(1, 3, 5, 7).addEdge(5, 4).addEdge(5, 8).addEdge(1, 8)
                .sorted()
                .buildDigraph();
         */
        //var g = GraphBuilder.numVertices(3).addEdges("0-0,1-1,0-1,1-0").buildPseudograph();
        //var g = GraphBuilder.vertexRange(1, 5).addEdges("1-2,2-3,3-4,4-5").addEdge(2,4).buildGraph();
        var g = GraphBuilder.vertexRange(1, 5).addEdges("1-2,1-3,1-4,1-5").buildGraph();

        //g.setName("K4");
        System.out.println(g);
        new BFSTraverser(g).traverse(new BFSVisitor() {
            @Override
            public void startVertex(SearchNode node) {
                System.out.println("Start vertex: " + node);
            }

            @Override
            public void finishVertex(SearchNode node, boolean leaf) {
                System.out.println("Finish vertex: " + node + ", leaf: " + leaf);
            }

            @Override
            public void treeEdge(SearchNode from, SearchNode to) {
                System.out.println("Tree edge: " + from + "->" + to);
            }

            @Override
            public void backEdge(SearchNode from, SearchNode to) {
                System.out.println("Back edge: " + from + "->" + to);
            }

            @Override
            public void crossEdge(SearchNode from, SearchNode to) {
                System.out.println("Cross edge: " + from + "->" + to);
            }
        });
    }

    public static void main(String args[]) {
        new BFSVisitorExample().demoBFS();
    }
}
