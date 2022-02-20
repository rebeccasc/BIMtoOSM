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
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.PlacementResolver as Ifc2x3tc1_PlacementResolver
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.GeometryResolver as Ifc2x3tc1_GeometryResolver
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Factory
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement3D as Ifc2x3tc1_IfcAxis2Placement3D
import org.bimserver.models.ifc2x3tc1.IfcCartesianPoint as Ifc2x3tc1_IfcCartesianPoint
import org.bimserver.models.ifc2x3tc1.IfcDirection as Ifc2x3tc1_IfcDirection
import de.rebsc.bimtoosm.geometry.ifc4.PlacementResolver as Ifc4_PlacementResolver
import de.rebsc.bimtoosm.geometry.ifc4.GeometryResolver as Ifc4_GeometryResolver
import org.bimserver.models.ifc4.Ifc4Factory
import org.bimserver.models.ifc4.IfcAxis2Placement3D as Ifc4_IfcAxis2Placement3D
import org.bimserver.models.ifc4.IfcCartesianPoint as Ifc4_IfcCartesianPoint
import org.bimserver.models.ifc4.IfcDirection as Ifc4_IfcDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class PlacementResolverTest {

    @Test
    fun getAbsolutePointTest_Ifc2x3tc1() {
        val resolver = Ifc2x3tc1_PlacementResolver()
        val placement3D: Ifc2x3tc1_IfcAxis2Placement3D = createAxisPlacement_Ifc2x3tc1(
            listOf(0.0, 0.0, 0.0),
            listOf(0.0, 0.0, 1.0),
            listOf(1.0, 0.0, 0.0)
        )
        val transformation = resolver.getTransform(placement3D)
        val absolutePoint: Point3D = resolver.getAbsolutePoint(transformation, 5.0, 0.0, 0.0)
        assertEquals(5.0, absolutePoint.x)
        assertEquals(0.0, absolutePoint.y)
        assertEquals(0.0, absolutePoint.z)
    }

    @Test
    fun getAbsolutePointTest_Ifc4() {
        val resolver = Ifc4_PlacementResolver()
        val placement3D: Ifc4_IfcAxis2Placement3D = createAxisPlacement_Ifc4(
            listOf(0.0, 0.0, 0.0),
            listOf(0.0, 0.0, 1.0),
            listOf(1.0, 0.0, 0.0)
        )
        val transformation = resolver.getTransform(placement3D)
        val absolutePoint: Point3D = resolver.getAbsolutePoint(transformation, 5.0, 0.0, 0.0)
        assertEquals(5.0, absolutePoint.x)
        assertEquals(0.0, absolutePoint.y)
        assertEquals(0.0, absolutePoint.z)
    }


    // helper

    private fun createAxisPlacement_Ifc2x3tc1(
        locationCoords: List<Double>,
        axisCoords: List<Double>,
        refDirectionCoords: List<Double>
    ): Ifc2x3tc1_IfcAxis2Placement3D {
        val placement3D = Ifc2x3tc1Factory.eINSTANCE.createIfcAxis2Placement3D()
        val location: Ifc2x3tc1_IfcCartesianPoint = Ifc2x3tc1Factory.eINSTANCE.createIfcCartesianPoint()
        location.coordinates.addAll(locationCoords)
        val axis: Ifc2x3tc1_IfcDirection = Ifc2x3tc1Factory.eINSTANCE.createIfcDirection()
        axis.directionRatios.addAll(axisCoords)
        val refDir: Ifc2x3tc1_IfcDirection = Ifc2x3tc1Factory.eINSTANCE.createIfcDirection()
        refDir.directionRatios.addAll(refDirectionCoords)
        placement3D.location = location
        placement3D.axis = axis
        placement3D.refDirection = refDir
        return placement3D
    }

    private fun createAxisPlacement_Ifc4(
        locationCoords: List<Double>,
        axisCoords: List<Double>,
        refDirectionCoords: List<Double>
    ): Ifc4_IfcAxis2Placement3D {
        val placement3D = Ifc4Factory.eINSTANCE.createIfcAxis2Placement3D()
        val location: Ifc4_IfcCartesianPoint = Ifc4Factory.eINSTANCE.createIfcCartesianPoint()
        location.coordinates.addAll(locationCoords)
        val axis: Ifc4_IfcDirection = Ifc4Factory.eINSTANCE.createIfcDirection()
        axis.directionRatios.addAll(axisCoords)
        val refDir: Ifc4_IfcDirection = Ifc4Factory.eINSTANCE.createIfcDirection()
        refDir.directionRatios.addAll(refDirectionCoords)
        placement3D.location = location
        placement3D.axis = axis
        placement3D.refDirection = refDir
        return placement3D
    }
}