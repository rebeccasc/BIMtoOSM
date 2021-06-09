package de.rebsc.bimtoosm.optimizer.oio.tools

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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

import de.rebsc.bimtoosm.utils.math.Point2D
import kotlin.math.*
import kotlin.properties.Delegates

/**
 * DBSCAN clustering algorithm
 */
class DBSCAN {

    private lateinit var points: ArrayList<Point2D>
    private var maxDistance by Delegates.notNull<Double>()
    private var minPoints by Delegates.notNull<Int>()
    private var visited = HashSet<Point2D>()

    /**
     * Gets neighbours of a given input value
     */
    private fun getNeighbours(point: Point2D): ArrayList<Point2D> {
        val neighbours = ArrayList<Point2D>()
        for (i in 0 until points.size) {
            val candidate = points[i]
            val dist = distance(point.y, candidate.y, point.x, candidate.x, 0.0, 0.0)
            if (dist <= maxDistance) {
                neighbours.add(candidate)
            }
        }
        return neighbours
    }

    /**
     * Merges the elements of two neighbours collections
     */
    private fun mergeNeighboursCollection(
        neighbours1: ArrayList<Point2D>,
        neighbours2: ArrayList<Point2D>
    ): ArrayList<Point2D> {
        for (i in neighbours2.indices) {
            val tempPt = neighbours2[i]
            if (!neighbours1.contains(tempPt)) {
                neighbours1.add(tempPt)
            }
        }
        return neighbours1
    }

    /**
     * Runs clustering and returns a collection of clusters
     *
     * @param points input points to cluster
     * @param maxDistance of cluster in meter
     * @param minPoints a cluster can hold
     * @return [ArrayList] including [ArrayList]s representing clusters
     * @throws DBSCANClusteringException
     */
    fun performClustering(
        points: ArrayList<Point2D>,
        maxDistance: Double,
        minPoints: Int
    ): ArrayList<ArrayList<Point2D>> {
        this.points = points
        this.maxDistance = maxDistance
        this.minPoints = minPoints

        if (points.isEmpty()) {
            throw DBSCANClusteringException("List of input values empty")
        }
        if (points.size < 2) {
            throw DBSCANClusteringException("Too less points to cluster. Number of input values: " + points.size)
        }
        if (maxDistance < 0) {
            throw DBSCANClusteringException("<maxDistance> set smaller than 0: $maxDistance")
        }

        val resultList = ArrayList<ArrayList<Point2D>>()
        visited.clear()
        var neighbours: ArrayList<Point2D>
        var index = 0

        while (points.size > index) {
            val point = points[index]
            if (!visited.contains(point)) {
                visited.add(point)
                neighbours = getNeighbours(point)
                if (neighbours.size >= minPoints) {
                    var ind = 0
                    while (neighbours.size > ind) {
                        val right = neighbours[ind]
                        if (!visited.contains(right)) {
                            visited.add(right)
                            val indNeighbours = getNeighbours(right)
                            if (indNeighbours.size >= minPoints) {
                                neighbours = mergeNeighboursCollection(neighbours, indNeighbours)
                            }
                        }
                        ++ind
                    }
                    resultList.add(neighbours)
                }
            }
            ++index
        }
        return resultList
    }


    /**
     * Calculates distance in meter between two coordinates (lat, lon, el)
     *
     * @param lat1 latitude of coordinate 1
     * @param lat2 latitude of coordinate 2
     * @param lon1 longitude of coordinate 1
     * @param lon2 longitude of coordinate 2
     * @param el1 elevation of coordinate 1
     * @param el2 elevation of coordinate 2
     * @return distance in meter
     */
    private fun distance(
        lat1: Double, lat2: Double,
        lon1: Double, lon2: Double,
        el1: Double, el2: Double
    ): Double {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
                * sin(lonDistance / 2) * sin(lonDistance / 2)))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = R * c * 1000 // convert to meters
        val height = el1 - el2
        distance = distance.pow(2.0) + height.pow(2.0)
        return sqrt(distance)
    }

    class DBSCANClusteringException(msg: String) : Exception(msg)

}