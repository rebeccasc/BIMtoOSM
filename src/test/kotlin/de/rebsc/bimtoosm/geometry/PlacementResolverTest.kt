package de.rebsc.bimtoosm.geometry

/******************************************************************************
 * Copyright (c) 2010-2017 Helga Tauscher
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

import de.rebsc.bimtoosm.utils.math.Point3D
import org.bimserver.models.ifc4.Ifc4Factory
import org.bimserver.models.ifc4.IfcAxis2Placement3D
import org.bimserver.models.ifc4.IfcCartesianPoint
import org.bimserver.models.ifc4.IfcDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class PlacementResolverTest {

    @Test
    fun getAbsolutePointTest() {
        val resolver = PlacementResolver()
        val placement3D: IfcAxis2Placement3D = createAxisPlacement(
            listOf(0.0, 0.0, 0.0),
            listOf(0.0, 0.0, 1.0),
            listOf(1.0, 0.0, 0.0)
        )
        val transformation = resolver.getTransform(placement3D)
        val absolutePoint: Point3D = PlacementResolver().getAbsolutePoint(transformation, 5.0, 0.0, 0.0)
        assertEquals(5.0, absolutePoint.x)
        assertEquals(0.0, absolutePoint.y)
        assertEquals(0.0, absolutePoint.z)
    }

    private fun createAxisPlacement(
        locationCoords: List<Double>,
        axisCoords: List<Double>,
        refDirectionCoords: List<Double>
    ): IfcAxis2Placement3D {
        val placement3D = Ifc4Factory.eINSTANCE.createIfcAxis2Placement3D()
        val location: IfcCartesianPoint = Ifc4Factory.eINSTANCE.createIfcCartesianPoint()
        location.coordinates.addAll(locationCoords)
        val axis: IfcDirection = Ifc4Factory.eINSTANCE.createIfcDirection()
        axis.directionRatios.addAll(axisCoords)
        val refDir: IfcDirection = Ifc4Factory.eINSTANCE.createIfcDirection()
        refDir.directionRatios.addAll(refDirectionCoords)
        placement3D.location = location
        placement3D.axis = axis
        placement3D.refDirection = refDir
        return placement3D
    }
}