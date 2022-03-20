package de.rebsc.bimtoosm.geometry.ifc2x3

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
import de.rebsc.bimtoosm.geometry.GeometrySolution
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3PlacementResolver
import de.rebsc.bimtoosm.loader.Loader
import de.rebsc.bimtoosm.optimizer.BIMFileOptimizer
import de.rebsc.bimtoosm.utils.IdGenerator
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import jdk.jfr.Description
import org.bimserver.models.ifc2x3tc1.IfcWall
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.net.URL


internal class Ifc2x3ResolveWallTest {
    // Test setup

    // URLs
    private val urlWallWithWindow =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/wall_single_with_window_IFC2X3.ifc")
    private val urlWallWithWindowResolvedGeo =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/resolved_placements/wall_single_with_window_IFC2X3.txt")
    private val urlWallCrossing =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/wall_crossing_IFC2X3.ifc")
    private val urlWallCrossing1ResolvedGeo =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/resolved_placements/wall_1_crossing_IFC2X3.txt")
    private val urlWallSingle =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwall/wall_single_IFC2X3.ifc")
    private val urlWallSingleResolvedGeo =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwall/resolved_placements/wall_single_IFC2X3.txt")

    // Parser
    private val placementResolver = Ifc2x3PlacementResolver()
    private val geometryResolverBody = Ifc2x3GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB = Ifc2x3GeometryResolver(GeometrySolution.BOUNDING_BOX)

    private val connector: MutableMap<Long, Long> = HashMap()

    @Test
    @Description("IfcWallStandardCase test 1 for IFC2X3 on geometry solution BODY")
    fun resolveWallTestBody1() {
        // load optimized file into model
        val fileWallWithWindow = downloadFile(urlWallWithWindow)
        val fileWallWithWindowOptimized: String = BIMFileOptimizer.optimizeIfcFile(
            fileWallWithWindow, optimizeInput_RBC = true, optimizeInput_RBL = true
        ).absolutePath
        val model = Loader.loadIntoModel(fileWallWithWindowOptimized)

        clearCaches()

        // fill placement cache and geometry cache with wall objects
        model.getAllWithSubTypes(IfcWall::class.java).forEach { wall ->
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver.resolvePlacement(wall.objectPlacement)
            geometryResolverBody.resolveWall(wall.representation)
        }

        // extract walls out of placement cache and geometry cache
        val walls = extractWays_Ifc2x3tc1(geometryResolverBody, placementResolver, connector)

        // check if only one wall in list
        Assertions.assertEquals(1, walls.size)

        // check resolved geometry
        val fileWallWithWindowResolvedGeo = downloadFile(urlWallWithWindowResolvedGeo)
        val resolvedCoords = loadResolvedGeometry(fileWallWithWindowResolvedGeo)
        for (i in 0 until walls[0].points.size) {
            Assertions.assertEquals(resolvedCoords[i].x, walls[0].points[i].x, 0.1)
            Assertions.assertEquals(resolvedCoords[i].y, walls[0].points[i].y, 0.1)
        }

        // clean up test directory
        cleanTestDirectory()
    }

    @Test
    @Description("IfcWallStandardCase test 1 for IFC2X3 on geometry solution BOUNDINGBOX")
    fun resolveWallTestBB1() {
        // TODO implement
    }

    @Test
    @Description("IfcWallStandardCase test 2 for IFC2X3 on geometry solution BODY")
    fun resolveWallTestBody2() {
        // load optimized file into model
        val fileWallCrossing = downloadFile(urlWallCrossing)
        val fileWallCrossingOptimized: String = BIMFileOptimizer.optimizeIfcFile(
            fileWallCrossing, optimizeInput_RBC = true, optimizeInput_RBL = true
        ).absolutePath
        val model = Loader.loadIntoModel(fileWallCrossingOptimized)

        clearCaches()

        // fill placement cache and geometry cache with wall objects
        model.getAllWithSubTypes(IfcWall::class.java).forEach { wall ->
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver.resolvePlacement(wall.objectPlacement)
            geometryResolverBody.resolveWall(wall.representation)
        }

        // extract walls out of placement cache and geometry cache
        val walls = extractWays_Ifc2x3tc1(geometryResolverBody, placementResolver, connector)

        // check if only one wall in list
        Assertions.assertEquals(2, walls.size)

        // check resolved geometry
        val fileWallCrossing1ResolvedGeo = downloadFile(urlWallCrossing1ResolvedGeo)
        val resolvedCoords = loadResolvedGeometry(fileWallCrossing1ResolvedGeo)

        // there is no warranty for order in 'walls', find the one you want to test
        val wallToTest = walls.find { it.points[0].x == 892.89902 }

        // for now convert points from mm to m because this is not handled in engine
        for (i in 0 until wallToTest!!.points.size) {
            Assertions.assertEquals(resolvedCoords[i].x, wallToTest.points[i].x / 1000.0, 0.1)
            Assertions.assertEquals(resolvedCoords[i].y, wallToTest.points[i].y / 1000.0, 0.1)
        }

        // clean up test directory
        cleanTestDirectory()
    }

    @Test
    @Description("IfcWallStandardCase test 2 for IFC2X3 on geometry solution BOUNDINGBOX")
    fun resolveWallTestBB2() {
        // TODO implement
    }

    @Test
    @Description("IfcWall test for IFC2X3 on geometry solution BODY")
    fun resolveWallTestBody3() {
        // load optimized file into model
        val fileWallSingle = downloadFile(urlWallSingle)
        val fileWallSingleOptimized: String = BIMFileOptimizer.optimizeIfcFile(
            fileWallSingle, optimizeInput_RBC = true, optimizeInput_RBL = true
        ).absolutePath
        val model = Loader.loadIntoModel(fileWallSingleOptimized)

        clearCaches()

        // fill placement cache and geometry cache with wall objects
        model.getAllWithSubTypes(IfcWall::class.java).forEach { wall ->
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver.resolvePlacement(wall.objectPlacement)
            geometryResolverBody.resolveWall(wall.representation)
        }

        // extract walls out of placement cache and geometry cache
        val walls = extractWays_Ifc2x3tc1(geometryResolverBody, placementResolver, connector)

        // check if only one wall in list
        Assertions.assertEquals(1, walls.size)

        // check resolved geometry
        val fileWallSingleResolvedGeo = downloadFile(urlWallSingleResolvedGeo)
        val resolvedCoords = loadResolvedGeometry(fileWallSingleResolvedGeo)
        // for now convert points from mm to m because this is not handled in engine
        for (i in 0 until walls[0].points.size) {
            Assertions.assertEquals(resolvedCoords[i].x, walls[0].points[i].x / 1000.0, 0.1)
            Assertions.assertEquals(resolvedCoords[i].y, walls[0].points[i].y / 1000.0, 0.1)
        }

        // clean up test directory
        cleanTestDirectory()
    }

    @Test
    @Description("IfcWall test for IFC2X3 on geometry solution BOUNDINGBOX")
    fun resolveWallTestBB3() {
        // TODO implement
    }

    // helper

    private fun clearCaches() {
        connector.clear()
        geometryResolverBB.geometryCacheIfc2x3tc1.clear()
        placementResolver.placementCacheIfc2x3tc1.clear()
    }

    private fun extractWays_Ifc2x3tc1(
        geometryResolver: Ifc2x3GeometryResolver,
        placementResolver: Ifc2x3PlacementResolver,
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
                osmNodeList.add(OSMNode(IdGenerator.createUUID(allowNegative = true), Point2D(absolutePoint.x, absolutePoint.y)))
            }
            wayList.add(OSMWay(representation.key.productRepresentation.expressId, osmNodeList))
        }

        return wayList
    }

    private fun loadResolvedGeometry(file: File): ArrayList<Point2D> {
        // follow file schema expected:
        // point,value x-coordinate,value y-coordinate

        // list of x,y-pairs
        val coords = ArrayList<Point2D>()
        file.forEachLine { line ->
            if (!line.startsWith("#")) {
                val x = line.split(",")[1].toDouble()
                val y = line.split(",")[2].toDouble()
                coords.add(Point2D(x, y))
            }
        }
        return coords
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

    private fun cleanTestDirectory() {
        val directoryPath = "${System.getProperty("user.dir")}/src/test/tmp_test".replace("/", File.separator)
        File(directoryPath).deleteRecursively()
    }

}