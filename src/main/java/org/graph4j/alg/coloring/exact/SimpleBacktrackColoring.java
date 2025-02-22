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
package org.graph4j.alg.coloring.exact;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import org.graph4j.Graph;
import org.graph4j.alg.clique.MaximalCliqueFinder;
import org.graph4j.alg.coloring.VertexColoring;

/**
 * Attempts at finding the optimum coloring of a graph using a systematic
 * exploration of the search space. The backtracking is implemented in a
 * non-recursive manner.
 *
 * <p>
 * First, a maximal clique is computed that offers a lower bound <code>q</code>
 * of the chromatic number. The colors of the vertices in the maximal clique are
 * fixed before the backtracking algorithm starts.
 *
 * Secondly, an initial coloring is computed using a simple heuristic (DSatur).
 * This gives an upper bound <code>k</code>of the chromatic number.
 *
 * Next, the algorithm will attemtp to color the graph using a number of colors
 * ranging from <code>k-1</code> to <code>q</code>, determining the optimal
 * coloring.
 *
 * <p>
 * A time limit may be imposed. If the algorithm stops due to the time limit, it
 * will return the best coloring found until then.
 *
 * @author Cristian Frăsinaru
 */
public class SimpleBacktrackColoring extends ExactColoringBase {

    private Deque<Node> nodeStack;
    private int[][] assignQueue;
    private boolean include[][];

    public SimpleBacktrackColoring(Graph graph) {
        this(graph, 0);
    }

    public SimpleBacktrackColoring(Graph graph, long timeLimit) {
        super(graph, timeLimit);
        /*
        int n = graph.numVertices();
        this.include = new boolean[n][n];
        int x = 0;
        for (int i = 0; i < n - 1; i++) {
            int v = graph.vertexAt(i);
            for (int j = i + 1; j < n; j++) {
                int u = graph.vertexAt(j);
                VertexSet di = new VertexSet(graph, graph.neighbors(v));
                di.remove(u);
                VertexSet dj = new VertexSet(graph, graph.neighbors(u));
                dj.remove(v);
                include[i][j] = include[j][i] = di.equals(dj);
                if (include[i][j]) {
                    x++;
                }
            }
        }
        System.out.println("include " + x);
         */
    }

    @Override
    protected SimpleBacktrackColoring getInstance(Graph graph, long timeLimit) {
        return new SimpleBacktrackColoring(graph, timeLimit);
    }

        
    @Override
    public VertexColoring findColoring(int numColors) {
        if (!init(numColors)) {
            return null;
        }
        while (!nodeStack.isEmpty()) {
            if (timeLimit > 0 && System.currentTimeMillis() - startTime > timeLimit) {
                return null;
            }
            Node node = nodeStack.peek();
            if (node.coloring.isComplete()) {
                nodeStack.poll();
                if (!node.coloring.isProper()) {
                    continue;
                }
                return node.coloring; //solution found
            }
            //select the node's domain
            Domain domain = node.minDomain;
            assert domain != null;
            if (domain.size == 0) {
                nodeStack.poll();
                continue;
            }

            //pick a color in the domain
            int v = domain.vertex;
            int color = domain.poll();

            //create the new domains (lazy)
            Domain[] newDomains = Arrays.copyOf(node.domains, node.domains.length);
            //the domain of the selected vertex v becomes singleton
            newDomains[graph.indexOf(v)] = new Domain(v, color); //TODO - Singleton

            //create the new coloring
            //color and propagate the assignment v=c
            var newColoring = new VertexColoring(graph, node.coloring);
            newColoring.setColor(v, color);

            Node newNode = new Node(graph, node, v, color, newDomains, newColoring, false);
            if (propagateAssignment(v, color, newNode)) {
                newNode.prepare();
                nodeStack.push(newNode);
            }
        }
        return null;
    }

    //before findColoring
    private boolean init(int numColors) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (maxClique == null) {
            maxClique = new MaximalCliqueFinder(graph).getMaximalClique();
        }
        if (maxClique.size() > numColors) {
            return false;
        }
        int[] initialColors = new int[numColors];
        for (int i = 0; i < numColors; i++) {
            initialColors[i] = numColors - i - 1;
        }
        int n = graph.numVertices();
        this.nodeStack = new ArrayDeque<>();
        this.assignQueue = new int[n * numColors][2];
        Domain[] domains = new Domain[n];
        VertexColoring coloring = new VertexColoring(graph);
        int color = 0;
        for (int i = 0; i < n; i++) {
            int v = graph.vertexAt(i);
            if (maxClique.contains(v)) {
                coloring.setColor(v, color);
                domains[i] = new Domain(v, color); //TODO - Singleton
                color++;
                continue;
            }
            domains[i] = new Domain(v, initialColors);
        }
        Node root = new Node(graph, null, -1, -1, domains, coloring, false);
        for (int v : maxClique.vertices()) {
            if (!propagateAssignment(v, coloring.getColor(v), root)) {
                return false;
            }
            color++;
        }
        root.prepare();
        nodeStack.push(root);
        return true;
    }

    //after the assignment v=color, prunte the other domains
    private boolean propagateAssignment(int v, int color, Node node) {
        var coloring = node.coloring;
        var domains = node.domains;
        int i = 0, j = 1;
        assignQueue[i][0] = v;
        assignQueue[i][1] = color;
        while (i < j) {
            v = assignQueue[i][0];
            color = assignQueue[i][1];
            i++;
            for (var it = graph.neighborIterator(v); it.hasNext();) {
                int u = it.next();
                if (coloring.isColorSet(u)) {
                    continue;
                }
                int ui = graph.indexOf(u);
                var dom = domains[ui];
                int pos = dom.indexOf(color);
                if (pos < 0) {
                    continue;
                }
                if (dom.size == 1) {
                    return false;
                }
                if (node.parent != null && dom == node.parent.domains[ui]) {
                    dom = new Domain(dom);
                    domains[ui] = dom;
                }
                dom.removeAtPos(pos);
                if (dom.size == 1) {
                    assignQueue[j][0] = u;
                    assignQueue[j][1] = dom.values[0];
                    j++;
                }
            }
        }

        return true;
    }

}
