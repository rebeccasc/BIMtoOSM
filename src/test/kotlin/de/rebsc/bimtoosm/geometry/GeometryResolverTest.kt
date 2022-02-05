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

import de.rebsc.bimtoosm.data.osm.OSMNode
import de.rebsc.bimtoosm.data.osm.OSMWay
import de.rebsc.bimtoosm.loader.Loader
import de.rebsc.bimtoosm.optimizer.BIMFileOptimizer
import de.rebsc.bimtoosm.parser.Configuration
import de.rebsc.bimtoosm.parser.ParserStatus
import de.rebsc.bimtoosm.parser.PropertiesExtractor
import de.rebsc.bimtoosm.utils.IdGenerator
import org.bimserver.models.ifc2x3tc1.IfcColumn as Ifc2x3tc1_IfcColumn
import org.bimserver.models.ifc2x3tc1.IfcDoor as Ifc2x3tc1_IfcDoor
import org.bimserver.models.ifc2x3tc1.IfcSlab as Ifc2x3tc1_IfcSlab
import org.bimserver.models.ifc2x3tc1.IfcStair as Ifc2x3tc1_IfcStair
import org.bimserver.models.ifc2x3tc1.IfcWall as Ifc2x3tc1_IfcWall
import org.bimserver.models.ifc2x3tc1.IfcWindow as Ifc2x3tc1_IfcWindow
import org.bimserver.models.ifc2x3tc1.IfcSlabTypeEnum as Ifc2x3tc1_IfcSlabTypeEnum
import org.bimserver.models.ifc4.IfcColumn as Ifc4_IfcColumn
import org.bimserver.models.ifc4.IfcDoor as Ifc4_IfcDoor
import org.bimserver.models.ifc4.IfcSlab as Ifc4_IfcSlab
import org.bimserver.models.ifc4.IfcStair as Ifc4_IfcStair
import org.bimserver.models.ifc4.IfcWall as Ifc4_IfcWall
import org.bimserver.models.ifc4.IfcWindow as Ifc4_IfcWindow
import org.bimserver.models.ifc4.IfcSlabTypeEnum as Ifc4_IfcSlabTypeEnum
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*


internal class GeometryResolverTest {

    // Test setup
    private val dir = System.getProperty("user.dir")
    private val placementResolver = PlacementResolver()
    private val geometryResolverBody = GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB = GeometryResolver(GeometrySolution.BOUNDING_BOX)
    private val connector: MutableMap<Long, Long> = HashMap()

    @Test
    fun resolveWallTest_Ifc2x3tc1() {

        // load file and optimize
        val ifcFilepath_Ifc2x3tc1: String = BIMFileOptimizer.optimizeIfcFile(
            "$dir\\src\\test\\resources\\geometry_engine\\wall_with_window_IFC2X3.ifc",
            optimizeInput_RBC = true,
            optimizeInput_RBL = true
        ).absolutePath
        val model = Loader.loadIntoModel(ifcFilepath_Ifc2x3tc1)

        //------------ test GeometrySolution.BODY ------------ //
        clearCaches()
        model.getAllWithSubTypes(Ifc2x3tc1_IfcWall::class.java).forEach { wall ->
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver.resolvePlacement(wall.objectPlacement)
            geometryResolverBody.resolveWall(wall.representation)
        }

        // check coordinates
        val walls = extractWays(geometryResolverBody, placementResolver, connector)
        assertEquals(walls[0].points[0].x, 0.0)
        assertEquals(walls[0].points[0].y, 0.0)
        assertEquals(walls[0].points[1].x, 0.0)
        assertEquals(walls[0].points[1].y, 0.3)
        assertEquals(walls[0].points[2].x, 5.0)
        assertEquals(walls[0].points[2].y, 0.3)
        assertEquals(walls[0].points[3].x, 5.0)
        assertEquals(walls[0].points[3].y, 0.0)
        assertEquals(walls[0].points[4].x, walls[0].points[0].x)
        assertEquals(walls[0].points[4].y, walls[0].points[0].y)

        //------------ test GeometrySolution.BOUNDINGBOX ------------ //
        clearCaches()
        // TODO implement
    }

    @Test
    fun resolveWallTest_Ifc4() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveWall()
    }

    @Test
    fun resolveSlabTest_Ifc2x3tc1() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc2x3tc1 resolveSlab()
    }

    @Test
    fun resolveSlabTest_Ifc4() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveSlab()
    }


    @Test
    fun resolveColumnTest_Ifc2x3tc1() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc2x3tc1 resolveColumn()
    }

    @Test
    fun resolveColumnTest_Ifc4() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveColumn()
    }

    @Test
    fun resolveDoorTest_Ifc2x3tc1() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc2x3tc1 resolveDoor()
    }

    @Test
    fun resolveDoorTest_Ifc4() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveDoor()
    }

    @Test
    fun resolveWindowTest_Ifc2x3tc1() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc2x3tc1 resolveWindow()
    }

    @Test
    fun resolveWindowTest_Ifc4() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveWindow()
    }

    @Test
    fun resolveStairTest_Ifc2x3tc1() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc2x3tc1 resolveStair()
    }

    @Test
    fun resolveStairTest_Ifc4() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveStair()
    }

    // helper

    private fun clearCaches() {
        connector.clear()
        geometryResolverBB.geometryCacheIfc2x3tc1.clear()
        placementResolver.placementCacheIfc2x3tc1.clear()
    }

    private fun extractWays(
        geometryResolver: GeometryResolver,
        placementResolver: PlacementResolver,
        connector: MutableMap<Long, Long>
    ): ArrayList<OSMWay> {

        val wayList: ArrayList<OSMWay> = ArrayList()

        geometryResolver.geometryCacheIfc2x3tc1.forEach { representation ->
            // find placement connected to representation
            val connectorPlacements =
                connector.filterValues { it == representation.key.productRepresentation.expressId }
            val connectorPlacementKey = connectorPlacements.entries.first().key
            val placements =
                placementResolver.placementCacheIfc2x3tc1.filterKeys { it.expressId == connectorPlacementKey }
            val placement = placements.entries.first()

            // transform representation using placement
            val osmNodeList = ArrayList<OSMNode>()
            representation.value.forEach { point ->
                val absolutePoint =
                    placementResolver.getAbsolutePoint(placement.value, Point3D(point.x, point.y, point.z))
                osmNodeList.add(OSMNode(-1, Point2D(absolutePoint.x, absolutePoint.y)))
            }
            wayList.add(OSMWay(representation.key.productRepresentation.expressId, osmNodeList))
        }

        return wayList
    }
}