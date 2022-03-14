package de.rebsc.bimtoosm.geometry.ifc4

import de.rebsc.bimtoosm.data.osm.OSMNode
import de.rebsc.bimtoosm.data.osm.OSMWay
import de.rebsc.bimtoosm.geometry.GeometrySolution
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import jdk.jfr.Description
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import de.rebsc.bimtoosm.geometry.ifc4.GeometryResolver as Ifc4_GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc4.PlacementResolver as Ifc4_PlacementResolver
import java.net.URL


internal class Ifc4GeometryResolverTest {

    // Test setup

    // URLs
    private val urlWallSquareOnSite =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc4/wall/ifcwall/wall_square_on_site_IFC4.ifc")

    // Parser
    private val placementResolver = Ifc4_PlacementResolver()
    private val geometryResolverBody = Ifc4_GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB = Ifc4_GeometryResolver(GeometrySolution.BOUNDING_BOX)


    @Test
    @Description("")
    fun resolveWall() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveWall()
    }

    @Test
    @Description("")
    fun resolveSlab() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveSlab()
    }

    @Test
    @Description("")
    fun resolveColumn() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveColumn()
    }

    @Test
    @Description("")
    fun resolveDoor() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveDoor()
    }

    @Test
    @Description("")
    fun resolveWindow() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveWindow()
    }

    @Test
    @Description("")
    fun resolveStair() {
        // TODO test with GeometrySolution.BODY
        // TODO test with GeometrySolution.BOUNDING_BOX
        // TODO check Ifc4 resolveStair()
    }

    // helpers
    private fun extractWays_Ifc4(
        geometryResolver: Ifc4_GeometryResolver,
        placementResolver: Ifc4_PlacementResolver,
        connector: MutableMap<Long, Long>
    ): ArrayList<OSMWay> {

        val wayList: ArrayList<OSMWay> = ArrayList()

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
                osmNodeList.add(OSMNode(-1, Point2D(absolutePoint.x, absolutePoint.y)))
            }
            wayList.add(OSMWay(representation.key.productRepresentation.expressId, osmNodeList))
        }

        return wayList
    }
}