package de.rebsc.bimtoosm.geometry.ifc2x3tc1

/******************************************************************************
 * Copyright (c) 2010-2017 hlg
 * http://github.com/hlg/GroovyIFC
 *
 * This file is part of Groovy Ifc Tools, which are distributed
 * under the terms of the GNU General Public License version 3
 *
 *
 * Modification: This file was modified by rebsc
 *  - supports org.bimserver.models.ifc2x3tc1 objects
 *  - uses de.rebsc.bimtoosm.utils.math instead of javax.vecmath
 *****************************************************************************/

import de.rebsc.bimtoosm.utils.math.Matrix3D
import de.rebsc.bimtoosm.utils.math.Point3D
import de.rebsc.bimtoosm.utils.math.Vector3D
import org.bimserver.models.ifc2x3tc1.IfcLocalPlacement as Ifc2x3tc1_IfcLocalPlacement
import org.bimserver.models.ifc2x3tc1.IfcObjectPlacement as Ifc2x3tc1_IfcObjectPlacement
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement2D as Ifc2x3tc1_IfcAxis2Placement2D
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement3D as Ifc2x3tc1_IfcAxis2Placement3D
import org.bimserver.models.ifc2x3tc1.IfcCartesianPoint as Ifc2x3tc1_IfcCartesianPoint

class Ifc2x3PlacementResolver {

    /**
     * Placement cache for ifc2x3tc1 objects
     */
    var placementCacheIfc2x3tc1: MutableMap<Ifc2x3tc1_IfcObjectPlacement, Transformation> = HashMap()


    /**
     * Resolve placement for ifc2x3tc1 IfcObjectPlacement
     * @param placement entity of object
     * @return [Transformation] or null
     */
    fun resolvePlacement(placement: Ifc2x3tc1_IfcObjectPlacement): Transformation? {
        if (placementCacheIfc2x3tc1.containsKey(placement)) return placementCacheIfc2x3tc1[placement]
        if (placement is Ifc2x3tc1_IfcLocalPlacement) { // TODO: other case IfcGridPlacement, use method overloading
            val localPlacement: Ifc2x3tc1_IfcLocalPlacement = placement
            if (localPlacement.relativePlacement is Ifc2x3tc1_IfcAxis2Placement3D) { // TODO other case IfcAxisPlacement2D
                val axis2placement3d: Ifc2x3tc1_IfcAxis2Placement3D =
                    localPlacement.relativePlacement as Ifc2x3tc1_IfcAxis2Placement3D
                val placementTransformation: Transformation = getTransform(axis2placement3d)
                if (localPlacement.placementRelTo != null) {
                    val relatedTransformation = resolvePlacement(localPlacement.placementRelTo)
                    placementTransformation.rotation.multiply(relatedTransformation!!.rotation)
                    placementTransformation.translation.set(
                        getAbsolutePoint(
                            relatedTransformation,
                            Point3D(placementTransformation.translation)
                        )
                    )
                }
                placementCacheIfc2x3tc1[placement] = placementTransformation
                return placementTransformation
            }
            throw UnsupportedOperationException(
                "found " + localPlacement.relativePlacement::class.java.name.toString() + " instead of IfcAxis2Placement3D"
            )
        }
        throw UnsupportedOperationException(
            "found " + placement::class.java.name.toString() + " instead of IfcLocalPlacement"
        )
    }

    fun getTransform(placement: Ifc2x3tc1_IfcAxis2Placement2D): Transformation {
        val location = vectorFor(placement.location.coordinates)
        val zAxis = Vector3D(0.0, 0.0, 1.0)
        val xAxis = if (placement.refDirection != null) vectorFor(
            placement.refDirection.directionRatios
        ) else Vector3D(1.0, 0.0, 0.0)
        return getTransformation(location, xAxis, zAxis)
    }

    fun getTransform(placement: Ifc2x3tc1_IfcAxis2Placement3D?): Transformation {
        if (placement == null) return getIdentityTransform()
        val location =
            if (placement.location != null) vectorFor(placement.location.coordinates) else Vector3D(
                0.0,
                0.0,
                0.0
            ) // should never be null
        val refDirection =
            if (placement.refDirection != null) vectorFor(placement.refDirection.directionRatios) else Vector3D(
                1.0,
                0.0,
                0.0
            )
        val zAxis = if (placement.axis != null) vectorFor(placement.axis.directionRatios) else Vector3D(
            0.0,
            0.0,
            1.0
        )
        val xAxis = retrieveXAxis(zAxis, refDirection)
        return getTransformation(location, xAxis, zAxis)
    }

    private fun getIdentityTransform(): Transformation {
        val vector3d = Vector3D(0.0, 0.0, 0.0)
        val matrix3d = Matrix3D()
        matrix3d.setIdentity()
        return Transformation(vector3d, matrix3d)
    }

    private fun getTransformation(location: Vector3D, xAxis: Vector3D, zAxis: Vector3D): Transformation {
        val xNorm = Vector3D()
        xNorm.normalize(xAxis)
        val yNorm = Vector3D()
        yNorm.cross(zAxis, xAxis)
        yNorm.normalize()
        val zNorm = Vector3D()
        zNorm.normalize(zAxis)
        return Transformation(
            location,
            Matrix3D(
                xNorm.x,
                xNorm.y,
                xNorm.z,
                yNorm.x,
                yNorm.y,
                yNorm.z,
                zNorm.x,
                zNorm.y,
                zNorm.z
            )
        )
    }

    fun getAbsolutePoint(placement: Transformation, point: Ifc2x3tc1_IfcCartesianPoint): Point3D {
        // point.getDim() is a derived property, not working in BimServer
        return getAbsolutePoint(
            placement,
            point.coordinates[0],
            point.coordinates[1],
            if (point.coordinates.size == 2) 0.0 else point.coordinates[2]
        )
    }

    fun getAbsolutePoint(placement: Transformation, x: Double, y: Double, z: Double): Point3D {
        val absolutePt = Point3D(x, y, z)
        getAbsolutePoint(placement, absolutePt)
        return absolutePt
    }

    fun getAbsolutePoint(placement: Transformation, absolutePt: Point3D): Point3D {
        val inverse = Matrix3D(placement.rotation) // TODO: do this once for placement only?
        inverse.invert()
        inverse.transform(absolutePt) // changes absolutePt [sic]
        absolutePt.add(placement.translation) // changes absolutePt
        return absolutePt
    }

    private fun retrieveXAxis(zAxis: Vector3D, refDirection: Vector3D): Vector3D {
        val d: Double = refDirection.dot(zAxis) / zAxis.lengthSquared()
        val xAxis = Vector3D(refDirection)
        val refZ = Vector3D(zAxis)
        refZ.scale(d)
        xAxis.sub(refZ)
        return xAxis
    }

    private fun vectorFor(coords: List<Double>): Vector3D {
        return Vector3D(coords[0], coords[1], if (coords.size > 2.0) coords[2] else 0.0)
    }

    class Transformation(var translation: Vector3D, var rotation: Matrix3D)

}