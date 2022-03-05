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
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.net.URL
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.GeometryResolver as Ifc2x3tc1_GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.PlacementResolver as Ifc2x3tc1_PlacementResolver
import de.rebsc.bimtoosm.geometry.ifc4.GeometryResolver as Ifc4_GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc4.PlacementResolver as Ifc4_PlacementResolver
import org.bimserver.models.ifc2x3tc1.IfcWall as Ifc2x3tc1_IfcWall


internal class GeometryResolverTest {

    // Test setup

    // URLs
    private val url_wall_with_window_IFC2X3 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/wall_single_with_window_IFC2X3.ifc")

    // ifc4
    private val placementResolver_ifc4 = Ifc4_PlacementResolver()
    private val geometryResolverBody_ifc4 = Ifc4_GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB_ifc4 = Ifc4_GeometryResolver(GeometrySolution.BOUNDING_BOX)

    // ifc2x3tc1
    private val placementResolver_ifc2x3tc1 = Ifc2x3tc1_PlacementResolver()
    private val geometryResolverBody_ifc2x3tc1 = Ifc2x3tc1_GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB_ifc2x3tc1 = Ifc2x3tc1_GeometryResolver(GeometrySolution.BOUNDING_BOX)

    private val connector: MutableMap<Long, Long> = HashMap()

    @Test
    fun resolveWallTest_Ifc2x3tc1() {
        // load file and optimize
        val file_wall_with_window_IFC2X3 = downloadFile(url_wall_with_window_IFC2X3)
        // TODO load more files

        val ifcFilepath_Ifc2x3tc1: String = BIMFileOptimizer.optimizeIfcFile(
            file_wall_with_window_IFC2X3,
            optimizeInput_RBC = true,
            optimizeInput_RBL = true
        ).absolutePath
        val model = Loader.loadIntoModel(ifcFilepath_Ifc2x3tc1)

        //------------ test GeometrySolution.BODY ------------ //
        clearCaches()
        model.getAllWithSubTypes(Ifc2x3tc1_IfcWall::class.java).forEach { wall ->
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver_ifc2x3tc1.resolvePlacement(wall.objectPlacement)
            geometryResolverBody_ifc2x3tc1.resolveWall(wall.representation)
        }

        // check coordinates
        val walls = extractWays_Ifc2x3tc1(geometryResolverBody_ifc2x3tc1, placementResolver_ifc2x3tc1, connector)
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

        // clean up test directory
        cleanTestDirectory()
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
        geometryResolverBB_ifc2x3tc1.geometryCacheIfc2x3tc1.clear()
        placementResolver_ifc2x3tc1.placementCacheIfc2x3tc1.clear()
        geometryResolverBB_ifc4.geometryCacheIfc4.clear()
        placementResolver_ifc4.placementCacheIfc4.clear()
    }

    private fun extractWays_Ifc2x3tc1(
        geometryResolver: Ifc2x3tc1_GeometryResolver,
        placementResolver: Ifc2x3tc1_PlacementResolver,
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

    @Throws(IOException::class)
    private fun downloadFile(url: URL): File {
        // check if test directory already exists, if not create
        val directoryPath = "${System.getProperty("user.dir")}/src/test/tmp_test".replace("/", File.separator)
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdir()
        }
        // download file into test directory
        val tmpFile = File("$directoryPath${File.separator}tmpFileTest")
        try {
            val bytes = url.readBytes()
            tmpFile.writeBytes(bytes)
        } catch (e: IOException) {
            throw IOException("Could not download file $url. Abort test.")
        }
        return tmpFile
    }

    private fun cleanTestDirectory(){
        val directoryPath = "${System.getProperty("user.dir")}/src/test/tmp_test".replace("/", File.separator)
        File(directoryPath).deleteRecursively()
    }

}