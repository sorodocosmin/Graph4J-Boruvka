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
package ro.uaic.info.graph.temp;

import java.util.ArrayDeque;
import java.util.Queue;
import ro.uaic.info.graph.Digraph;

/**
 *
 * @author Cristian Frăsinaru
 */
public class TopologicalOrderIterator {

    private final Digraph digraph;
    private final int[] inDegrees;
    private final Queue<Integer> queue;

    /**
     *
     * @param digraph
     */
    public TopologicalOrderIterator(Digraph digraph) {
        this.digraph = digraph;
        this.inDegrees = digraph.inDegrees();
        int n = digraph.numVertices();
        this.queue = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            if (inDegrees[i] == 0) {
                queue.offer(digraph.vertexAt(i));
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    /**
     *
     * @return the topological order, or null if the digraph is not acyclic
     */
    public int next() {
        int v = queue.poll();
        for (int u : digraph.succesors(v)) {
            int ui = digraph.indexOf(u);
            inDegrees[ui]--;
            if (inDegrees[ui] == 0) {
                queue.offer(digraph.vertexAt(u));
            }
        }
        return v;
    }

}
