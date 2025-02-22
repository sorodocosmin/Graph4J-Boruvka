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
package org.graph4j.alg.connectivity;

import java.util.ArrayList;
import java.util.List;
import org.graph4j.Graph;
import org.graph4j.alg.SimpleGraphAlgorithm;
import org.graph4j.util.VertexSet;
import org.graph4j.util.VertexStack;
import org.graph4j.traverse.DFSVisitor;
import org.graph4j.traverse.DFSTraverser;
import org.graph4j.traverse.SearchNode;

/**
 * The algorithm for computing biconnected components in a connected undirected
 * graph is due to John Hopcroft and Robert Tarjan (1973). It runs in linear
 * time, and is based on depth-first search.
 *
 * A <em>block</em> is a maximal 2-connected subgraph.
 *
 * @author Cristian Frăsinaru
 */
public class TarjanBiconnectivity extends SimpleGraphAlgorithm
        implements BiconnectivityAlgorithm {

    private Boolean biconnected;
    private List<VertexSet> blocks;
    private VertexSet cutVertices;

    public TarjanBiconnectivity(Graph graph) {
        super(graph);
    }

    @Override
    public boolean isBiconnected() {
        if (biconnected != null) {
            return biconnected;
        }
        if (graph.numVertices() < 2) {
            return false;
        }
        compute(true);
        return biconnected;
    }

    @Override
    public VertexSet getCutVertices() {
        if (blocks == null) {
            getBlocks();
        }
        return cutVertices;
    }

    @Override
    public List<VertexSet> getBlocks() {
        if (blocks != null) {
            return blocks;
        }
        compute(false);
        return blocks;
    }

    private void compute(boolean checkOnly) {
        this.blocks = new ArrayList<>();
        this.cutVertices = new VertexSet(graph);
        var dfs = new DFSTraverser(graph);
        dfs.traverse(new Visitor(checkOnly));
        if (biconnected == null) {
            biconnected = true;
        }
        if (dfs.isInterrupted()) {
            blocks = null;
        }
    }

    private class Visitor implements DFSVisitor {

        private final boolean checkOnly;
        private final int[] low;
        private final VertexStack stack;

        public Visitor(boolean checkOnly) {
            this.checkOnly = checkOnly;
            this.low = new int[graph.numVertices()];
            this.stack = new VertexStack(graph);
        }

        @Override
        public void startVertex(SearchNode node) {
            int v = node.vertex();
            //by default, the lowpoint is the dfs visit time (order)
            low[graph.indexOf(v)] = node.order();
            stack.push(v);
        }

        @Override
        public void treeEdge(SearchNode from, SearchNode to) {
            int v = from.vertex();
            if (isRoot(from) && !stack.contains(v)) {
                cutVertices.add(v);
                stack.push(v);
            }
        }

        @Override
        public void backEdge(SearchNode from, SearchNode to) {
            //change the lowpoint of v=from.vertex
            int vi = graph.indexOf(from.vertex());
            low[vi] = Math.min(low[vi], to.order());
        }

        @Override
        public void upward(SearchNode from, SearchNode to) {
            int v = from.vertex();
            int u = to.vertex();
            int vi = graph.indexOf(v);
            int ui = graph.indexOf(u);
            //going up v -> u
            //the lowpoint of u is atleast the lowpoint of v 
            low[ui] = Math.min(low[ui], low[vi]);
            //
            if (low[vi] >= to.order()) {
                //u is an articulation point (cut vertex)
                //u and the vertices on the stack up to u form a block
                if (biconnected == null && blocks.size() > 0) {
                    biconnected = false;
                    if (checkOnly) {
                        interrupt();
                    }
                }
                createBlock(to);
            }
            if (!isRoot(to)) {
                stack.push(u);
            }
        }

        private void createBlock(SearchNode node) {
            int u = node.vertex();
            //u is an articulation point
            var block = new VertexSet(graph);
            int w;
            do {
                w = stack.pop();
                block.add(w);
            } while (w != u);
            blocks.add(block);
            if (!isRoot(node)) {
                cutVertices.add(u);
            }
        }
    }
}
