package de.rebsc.bimtoosm.optimizer.oio

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

import de.rebsc.bimtoosm.data.osm.OSMDataSet
import de.rebsc.bimtoosm.data.osm.OSMWay
import de.rebsc.bimtoosm.optimizer.oio.api.OSMIndoorOptimizer
import de.rebsc.bimtoosm.optimizer.oio.tools.DBSCAN
import de.rebsc.bimtoosm.optimizer.oio.tools.Merger
import de.rebsc.bimtoosm.utils.math.Point2D

class OSMOptimizer : OSMIndoorOptimizer {

    override fun mergeOverlaps(data: OSMDataSet): OSMDataSet {
        return Merger.mergeOverlaps(data)
    }

    override fun mergeCloseNodes(data: OSMDataSet, mergeDistance: Double): OSMDataSet {
        return Merger.mergeCloseNodes(data, mergeDistance)
    }

    override fun orthogonalizeShape(way: OSMWay): OSMWay {
        TODO("Not yet implemented")
    }

    override fun clusterPointsByDBSCAN(
        points: ArrayList<Point2D>,
        maxDistance: Double,
        minPoints: Int
    ): ArrayList<ArrayList<Point2D>> {
        return DBSCAN().performClustering(points, maxDistance, minPoints)
    }

}