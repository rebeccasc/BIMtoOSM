package de.rebsc.bimtoosm.geometry.ifc2x3

import de.rebsc.bimtoosm.data.osm.OSMNode
import de.rebsc.bimtoosm.data.osm.OSMWay
import de.rebsc.bimtoosm.geometry.GeometrySolution
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3PlacementResolver
import de.rebsc.bimtoosm.parser.IfcUnitPrefix
import de.rebsc.bimtoosm.utils.IdGenerator
import de.rebsc.bimtoosm.utils.UnitConverter
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import jdk.jfr.Description
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.net.URL

internal class Ifc2x3ResolveDoorTest {
    // Test setup

    // URLs

    // Parser
    private val placementResolver = Ifc2x3PlacementResolver()
    private val geometryResolverBody = Ifc2x3GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB = Ifc2x3GeometryResolver(GeometrySolution.BOUNDING_BOX)

    private val connector: MutableMap<Long, Long> = HashMap()


    @Test
    @Description("")
    fun resolveDoorTestBody() {
        // TODO implement
    }

    @Test
    @Description("")
    fun resolveDoorTestBB() {
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
        connector: MutableMap<Long, Long>,
        units: IfcUnitPrefix
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
                var absolutePoint =
                    placementResolver.getAbsolutePoint(placement.value, Point3D(point.x, point.y, point.z))
                // convert to meter if necessary
                absolutePoint = UnitConverter.toMeter(absolutePoint, units.lengthUnitPrefix)
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