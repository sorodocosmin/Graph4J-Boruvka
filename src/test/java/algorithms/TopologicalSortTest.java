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
package algorithms;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.graph4j.alg.ordering.AcyclicOrientation;
import org.graph4j.GraphBuilder;
import org.graph4j.alg.ordering.TopologicalSorting;

/**
 *
 * @author Cristian Frăsinaru
 */
public class TopologicalSortTest {

    @Test
    public void testTopo1() {
        var g = GraphBuilder.numVertices(5)
                .addEdges("4-3,4-2,4-1,4-0,3-2,3-1,3-0,2-1,2-0,1-0")
                .buildDigraph();
        assertArrayEquals(new int[]{4, 3, 2, 1, 0}, new TopologicalSorting(g).compute());                
    }

    @Test
    public void testTopo2() {
        var g = GraphBuilder.numVertices(5).addPath(0, 1, 2, 3, 4).buildGraph();
        
        var d1 = new AcyclicOrientation(g).create();
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, new TopologicalSorting(d1).compute());
        
        var d2 = new AcyclicOrientation(g, new int[]{4,3,2,1,0}).create();
        assertArrayEquals(new int[]{4, 3, 2, 1, 0}, new TopologicalSorting(d2).compute());
    }

}
