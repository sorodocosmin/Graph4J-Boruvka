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
package org.graph4j.alg.cut;

import org.graph4j.util.VertexSet;

/**
 * Utility class for representing a vertex separator set.
 *
 * @see GreedyVertexSeparator
 * @author Cristian Frăsinaru
 */
public class VertexSeparator {

    private final VertexSet separator;
    private final VertexSet leftShore;
    private final VertexSet rightShore;

    /**
     *
     * @param separator the separator vertex set.
     * @param leftShore the left shore.
     * @param rightShore the right shore.
     */
    public VertexSeparator(VertexSet separator, VertexSet leftShore, VertexSet rightShore) {
        this.separator = separator;
        this.leftShore = leftShore;
        this.rightShore = rightShore;
    }

    /**
     *
     * @return the separator.
     */
    public VertexSet separator() {
        return separator;
    }

    /**
     *
     * @return the left shore.
     */
    public VertexSet leftShore() {
        return leftShore;
    }

    /**
     *
     * @return the right shore.
     */
    public VertexSet rightShore() {
        return rightShore;
    }

    @Override
    public String toString() {
        return leftShore + "\n\t" + separator + "\n" + rightShore;
    }

}
