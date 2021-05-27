package de.rebsc.bimtoosm.geometry

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
import de.rebsc.bimtoosm.data.osm.OSMNode
import de.rebsc.bimtoosm.data.osm.OSMWay
import de.rebsc.bimtoosm.parser.IfcUnitPrefix
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import org.bimserver.emf.IfcModelInterface
import org.bimserver.emf.Schema
import org.bimserver.models.ifc2x3tc1.IfcColumn as Ifc2x3tc1_IfcColumn
import org.bimserver.models.ifc2x3tc1.IfcDoor as Ifc2x3tc1_IfcDoor
import org.bimserver.models.ifc2x3tc1.IfcSlab as Ifc2x3tc1_IfcSlab
import org.bimserver.models.ifc2x3tc1.IfcStair as Ifc2x3tc1_IfcStair
import org.bimserver.models.ifc2x3tc1.IfcWall as Ifc2x3tc1_IfcWall
import org.bimserver.models.ifc4.IfcColumn as Ifc4_IfcColumn
import org.bimserver.models.ifc4.IfcDoor as Ifc4_IfcDoor
import org.bimserver.models.ifc4.IfcSlab as Ifc4_IfcSlab
import org.bimserver.models.ifc4.IfcStair as Ifc4_IfcStair
import org.bimserver.models.ifc4.IfcWall as Ifc4_IfcWall


/**
 * Geometry engine solutions
 */
enum class GeometrySolution {

    BODY, BOUNDING_BOX
}

class GeometryEngine(private val solution: GeometrySolution) {

    /**
     * Transform each object in [model] into OSM object and put it into [OSMDataSet]
     * @param model ifc model
     * @param units ifc unit prefix
     * @return [OSMDataSet]
     */
    fun transformToOSM(model: IfcModelInterface, units: IfcUnitPrefix): OSMDataSet {
        val schema = model.modelMetaData.ifcHeader.ifcSchemaVersion

        // resolve placement and geometry
        val placementResolver = PlacementResolver()
        val geometryResolver = GeometryResolver(solution)
        // connect placement object with geometry object
        val connector: MutableMap<Long, Long> = HashMap()

        if (schema == Schema.IFC4.headerName) {
            model.getAllWithSubTypes(Ifc4_IfcWall::class.java).forEach { wall ->
                connector[wall.objectPlacement.expressId] = wall.representation.expressId
                placementResolver.resolvePlacement(wall.objectPlacement)
                geometryResolver.resolveWall(wall.representation)
            }
            model.getAllWithSubTypes(Ifc4_IfcSlab::class.java).forEach { slab ->
                connector[slab.objectPlacement.expressId] = slab.representation.expressId

                placementResolver.resolvePlacement(slab.objectPlacement)
                geometryResolver.resolveSlab(slab.representation)
            }
            model.getAllWithSubTypes(Ifc4_IfcColumn::class.java).forEach { column ->
                connector[column.objectPlacement.expressId] = column.representation.expressId
                placementResolver.resolvePlacement(column.objectPlacement)
                geometryResolver.resolveColumn(column.representation)
            }
            model.getAllWithSubTypes(Ifc4_IfcDoor::class.java).forEach { door ->
                connector[door.objectPlacement.expressId] = door.representation.expressId
                placementResolver.resolvePlacement(door.objectPlacement)
                geometryResolver.resolveDoor(door.representation)
            }
            model.getAllWithSubTypes(Ifc4_IfcStair::class.java).forEach { stair ->
                connector[stair.objectPlacement.expressId] = stair.representation.expressId
                placementResolver.resolvePlacement(stair.objectPlacement)
                geometryResolver.resolveStair(stair.representation)
            }
        }
        if (schema == Schema.IFC2X3TC1.headerName) {
            model.getAllWithSubTypes(Ifc2x3tc1_IfcWall::class.java).forEach { wall ->
                connector[wall.objectPlacement.expressId] = wall.representation.expressId
                placementResolver.resolvePlacement(wall.objectPlacement)
                geometryResolver.resolveWall(wall.representation)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcSlab::class.java).forEach { slab ->
                connector[slab.objectPlacement.expressId] = slab.representation.expressId
                placementResolver.resolvePlacement(slab.objectPlacement)
                geometryResolver.resolveSlab(slab.representation)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcColumn::class.java).forEach { column ->
                connector[column.objectPlacement.expressId] = column.representation.expressId
                placementResolver.resolvePlacement(column.objectPlacement)
                geometryResolver.resolveColumn(column.representation)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcDoor::class.java).forEach { door ->
                connector[door.objectPlacement.expressId] = door.representation.expressId
                placementResolver.resolvePlacement(door.objectPlacement)
                geometryResolver.resolveDoor(door.representation)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcStair::class.java).forEach { stair ->
                connector[stair.objectPlacement.expressId] = stair.representation.expressId
                placementResolver.resolvePlacement(stair.objectPlacement)
                geometryResolver.resolveStair(stair.representation)
            }
        }

        val osmDataSet = OSMDataSet()

        // transform geometry
        // TODO get proper UUID
        var id = -1
        geometryResolver.geometryCacheIfc4.forEach { representation ->
            // find placement connected to representation
            val connectorPlacements =
                connector.filterValues { it == representation.key.productRepresentation.expressId }
            val connectorPlacementKey = connectorPlacements.entries.first().key
            val placements = placementResolver.placementCacheIfc4.filterKeys { it.expressId == connectorPlacementKey }
            val placement = placements.entries.first()

            // transform representation using placement
            val osmNodeList = ArrayList<OSMNode>()
            representation.value.forEach { point ->
                val absolutePoint =
                    placementResolver.getAbsolutePoint(placement.value, Point3D(point.x, point.y, point.z))
                osmNodeList.add(OSMNode(id, Point2D(absolutePoint.x, absolutePoint.y)))
                --id
            }
            osmDataSet.addNodes(osmNodeList)
            osmDataSet.addWay(OSMWay(id, osmNodeList))
            --id
        }

        // TODO same for Ifc2x3tc1


        return osmDataSet
    }

}