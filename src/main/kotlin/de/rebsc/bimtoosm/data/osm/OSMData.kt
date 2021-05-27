package de.rebsc.bimtoosm.data.osm

import de.rebsc.bimtoosm.utils.math.Point2D

/******************************************************************************
 * Copyright (C) 2021  de.rebsc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

/**
 * Data set holding nodes and ways
 */
class OSMDataSet(val nodes: ArrayList<OSMNode>, val ways: ArrayList<OSMWay>) {

    constructor() : this(ArrayList<OSMNode>(), ArrayList<OSMWay>())

    fun addNodes(nodes: List<OSMNode>) {
        this.nodes.addAll(nodes)
    }

    fun addWays(ways: List<OSMWay>) {
        this.ways.addAll(ways)
    }

    fun addWay(way: OSMWay) {
        ways.add(way)
    }
}

/**
 * Node holding coordinates and tags
 */
data class OSMNode(val id: Int, val x: Double, val y: Double, val tags: List<OSMTag>) {

    constructor(id: Int, point: Point2D, tags: List<OSMTag>) : this(id, point.x, point.y, tags)
    constructor(id: Int, point: Point2D) : this(id, point.x, point.y, ArrayList<OSMTag>())
}

/**
 * Way holding nodes and tags
 */
data class OSMWay(val id: Int, val points: ArrayList<OSMNode>, val tags: ArrayList<OSMTag>) {

    constructor(id: Int, nodes: ArrayList<OSMNode>) : this(id, nodes, ArrayList<OSMTag>())
    constructor(id: Int) : this(id, ArrayList<OSMNode>(), ArrayList<OSMTag>())
}

/**
 * Object tag with key, value pair
 */
data class OSMTag(val k: String, val v: String)