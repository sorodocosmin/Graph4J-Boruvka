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
package org.graph4j.alg.coloring;

import java.util.Arrays;
import java.util.BitSet;
import org.graph4j.Graph;
import org.graph4j.alg.SimpleGraphAlgorithm;

/**
 * Greedy coloring is a simple heuristic algorithm that assigns colors to the
 * vertices of a graph in a greedy manner, that is, by selecting the smallest
 * possible color that has not yet been used by any of the neighboring vertices.
 *
 * The greedy algorithm does not necessarily produce the optimal vertex
 * coloring, but it can be a useful heuristic in certain cases.
 *
 *
 * @author Cristian Frăsinaru
 */
public abstract class GreedyColoringBase extends SimpleGraphAlgorithm
        implements VertexColoringAlgorithm {

    protected int[] colors; //the colors assigned to vertices
    protected BitSet used; // colors

    /**
     *
     * @param graph the input graph.
     */
    public GreedyColoringBase(Graph graph) {
        super(graph);
    }

    @Override
    public VertexColoring findColoring() {
        return findColoring(graph.numVertices());
    }

    @Override
    public VertexColoring findColoring(int numColors) {
        init();
        this.colors = new int[numColors];
        Arrays.fill(colors, -1);
        this.used = new BitSet();
        while (hasUncoloredVertices()) {
            int v = nextUncoloredVertex();
            //finding the colors used by the neighbors of v
            for (var it = graph.neighborIterator(v); it.hasNext();) {
                int u = it.next();
                int ui = graph.indexOf(u);
                if (colors[ui] >= 0) {
                    used.set(colors[ui]);
                }
            }
            //finding a color for v, not used by its neighbors
            int color = 0;
            while (used.get(color) && color < numColors - 1) {
                color++;
            }
            if (color == numColors) {
                return null;
            }
            colors[graph.indexOf(v)] = color;
            used.clear();
            update(v);
        }
        return new VertexColoring(graph, colors);
    }

    //called before the coloring algorithm starts
    protected void init() {
    }

    //called after a vertex has been colored
    protected void update(int v) {
    }

    protected abstract boolean hasUncoloredVertices();

    protected abstract int nextUncoloredVertex();

}
