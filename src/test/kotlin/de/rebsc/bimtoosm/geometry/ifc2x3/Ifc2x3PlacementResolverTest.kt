package de.rebsc.bimtoosm.geometry.ifc2x3

import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3PlacementResolver
import de.rebsc.bimtoosm.utils.math.Point3D
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Factory
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement3D
import org.bimserver.models.ifc2x3tc1.IfcCartesianPoint
import org.bimserver.models.ifc2x3tc1.IfcDirection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/******************************************************************************
 * Copyright (c) 2010-2017 hlg
 * http://github.com/hlg/GroovyIFC
 *
 * This file is part of Groovy Ifc Tools, which are distributed
 * under the terms of the GNU General Public License version 3
 *
 *
 *
 * Modification: This file was modified by rebsc
 *  - removed some test cases
 *****************************************************************************/

internal class Ifc2x3PlacementResolverTest {

    @Test
    fun getAbsolutePointTest_Ifc2x3tc1() {
        val resolver = Ifc2x3PlacementResolver()
        val placement3D: IfcAxis2Placement3D = createAxisPlacement_Ifc2x3tc1(
            listOf(0.0, 0.0, 0.0),
            listOf(0.0, 0.0, 1.0),
            listOf(1.0, 0.0, 0.0)
        )
        val transformation = resolver.getTransform(placement3D)
        val absolutePoint: Point3D = resolver.getAbsolutePoint(transformation, 5.0, 0.0, 0.0)
        Assertions.assertEquals(5.0, absolutePoint.x)
        Assertions.assertEquals(0.0, absolutePoint.y)
        Assertions.assertEquals(0.0, absolutePoint.z)
    }

    // helper

    private fun createAxisPlacement_Ifc2x3tc1(
        locationCoords: List<Double>,
        axisCoords: List<Double>,
        refDirectionCoords: List<Double>
    ): IfcAxis2Placement3D {
        val placement3D = Ifc2x3tc1Factory.eINSTANCE.createIfcAxis2Placement3D()
        val location: IfcCartesianPoint = Ifc2x3tc1Factory.eINSTANCE.createIfcCartesianPoint()
        location.coordinates.addAll(locationCoords)
        val axis: IfcDirection = Ifc2x3tc1Factory.eINSTANCE.createIfcDirection()
        axis.directionRatios.addAll(axisCoords)
        val refDir: IfcDirection = Ifc2x3tc1Factory.eINSTANCE.createIfcDirection()
        refDir.directionRatios.addAll(refDirectionCoords)
        placement3D.location = location
        placement3D.axis = axis
        placement3D.refDirection = refDir
        return placement3D
    }
}