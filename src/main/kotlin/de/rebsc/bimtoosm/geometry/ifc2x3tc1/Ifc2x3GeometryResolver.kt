package de.rebsc.bimtoosm.geometry.ifc2x3tc1

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

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import de.rebsc.bimtoosm.geometry.GeometrySolution
import de.rebsc.bimtoosm.geometry.SupportedObjectType
import de.rebsc.bimtoosm.logger.Logger
import de.rebsc.bimtoosm.utils.geometry.Geometry
import de.rebsc.bimtoosm.utils.math.Vector3D
import org.bimserver.models.ifc2x3tc1.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class Ifc2x3GeometryResolver(private val solution: GeometrySolution) {

    private val logger = Logger.get(this::class.java)

    /**
     * Geometry cache for ifc2x3tc1 objects
     */
    var geometryCacheIfc2x3tc1: MutableMap<Ifc2x3tc1RepresentationInfo, List<Vector3D>> = HashMap()


    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of wall object
     */
    fun resolveWall(productRepresentation: IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_WALL)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is IfcMaterialDefinitionRepresentation) {
            logger.info("${IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of slab object
     */
    fun resolveSlab(productRepresentation: IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_SLAB)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is IfcMaterialDefinitionRepresentation) {
            logger.info("${IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of column object
     */
    fun resolveColumn(productRepresentation: IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_COLUMN)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is IfcMaterialDefinitionRepresentation) {
            logger.info("${IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of door object
     */
    fun resolveDoor(productRepresentation: IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_DOOR)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is IfcMaterialDefinitionRepresentation) {
            logger.info("${IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of window object
     */
    fun resolveWindow(productRepresentation: IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_WINDOW)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is IfcMaterialDefinitionRepresentation) {
            logger.info("${IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of stair object
     */
    fun resolveStair(productRepresentation: IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_STAIR)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is IfcMaterialDefinitionRepresentation) {
            logger.info("${IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    // Utils

    /**
     * Resolve geometry of representation
     */
    private fun resolveGeometry(
        productRepresentation: IfcProductDefinitionShape,
        type: SupportedObjectType
    ): Boolean {
        val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis
        if (solution == GeometrySolution.BODY && repIndices.second != -1) {
            // body
            val rep = productRepresentation.representations[repIndices.second]
            geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationInfo(productRepresentation, rep, type)] = resolveBody(rep)
            return true
        }
        if (repIndices.first != -1) {
            // box
            val rep = productRepresentation.representations[repIndices.first]
            geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationInfo(productRepresentation, rep, type)] =
                resolveBoundingBox(rep)
            return true
        }
        if (repIndices.third != -1) {
            // axis
            val rep = productRepresentation.representations[repIndices.third]
            // TODO implement
            return true
        }
        return false
    }

    /**
     * Gets indices of representation types of [entity] in following order: boy, body, axis.
     * If not available set to default value (-1)
     * @param entity to get representation type indices of
     * @return triple with indices for box, body, axis representation
     */
    private fun getRepresentationsIndices(entity: IfcProductRepresentation): Triple<Int, Int, Int> {
        var box = -1
        var body = -1
        var axis = -1
        entity.representations.forEach { rep ->
            if (rep.representationIdentifier.equals("Box", true)) {
                box = entity.representations.indexOf(rep)
            }
            if (rep.representationIdentifier.equals("Body", true)) {
                body = entity.representations.indexOf(rep)
            }
            if (rep.representationIdentifier.equals("Axis", true)) {
                axis = entity.representations.indexOf(rep)
            }
        }
        return Triple(box, body, axis)
    }

    /**
     * Resolve coordinates from IfcShapeRepresentation subtype of body
     * @param shapeRepresentation parent of body entity
     * @return List holding resolved local coordinates
     */
    private fun resolveBody(shapeRepresentation: IfcRepresentation): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        shapeRepresentation.items.forEach { item ->
            when (item) {
                is IfcTopologicalRepresentationItem -> {
                    geometry.addAll(resolveIfcTopologicalRepresentationItem(item))
                }
                is IfcGeometricRepresentationItem -> {
                    geometry.addAll(resolveIfcGeometricRepresentationItem(item))
                }
                is IfcMappedItem -> {
                    // TODO implement
                }
                is IfcStyledItem -> {
                    // TODO implement
                }
            }

        }

        if (geometry.isEmpty()) {
            logger.warn("resolveBody-> Resolved geometry of ${shapeRepresentation.expressId} is empty")
        }
        return geometry
    }

    /**
     * Resolve coordinates from IfcShapeRepresentation type of IfcBoundingBox
     * @param shapeRepresentation parent of bounding box entity
     * @return List holding resolved local coordinates
     */
    private fun resolveBoundingBox(shapeRepresentation: IfcRepresentation): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        shapeRepresentation.items.forEach { item ->
            when (item) {
                is IfcGeometricRepresentationItem -> {
                    geometry.addAll(resolveIfcGeometricRepresentationItem(item))
                }
                is IfcMappedItem -> {
                    // TODO implement
                }
                is IfcStyledItem -> {
                    // TODO implement
                }
            }
        }
        if (geometry.isEmpty()) {
            logger.warn("resolveBoundingBox-> Resolved geometry of ${shapeRepresentation.expressId} is empty")
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcTopologicalRepresentationItem] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcTopologicalRepresentationItem(entity: IfcTopologicalRepresentationItem): List<Vector3D> {
        when (entity) {
            is IfcConnectedFaceSet -> {
                return resolveIfcConnectedFaceSet(entity)
            }
            is IfcEdge -> {
                // TODO implement
            }
            is IfcFace -> {
                return resolveIfcFace(entity)
            }
            is IfcFaceBound -> {
                // TODO implement
            }
            is IfcPath -> {
                // TODO implement
            }
            is IfcVertex -> {
                // TODO implement
            }
            is IfcLoop -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcConnectedFaceSet] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcConnectedFaceSet(entity: IfcConnectedFaceSet): List<Vector3D> {
        // check if closed shell or open shell
        when (entity) {
            is IfcClosedShell -> {
                return resolveIfcClosedShell(entity)
            }
            is IfcOpenShell -> {
                return resolveIfcOpenShell(entity)
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcClosedShell] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcClosedShell(entity: IfcClosedShell): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // resolve list of CfsFaces
        entity.cfsFaces.forEach { face ->
            // different faces may use the same coordinates so there are probably duplicates in list
            geometry.addAll(resolveIfcFace(face))
        }

        // remove duplicates
        // TODO check if its possible to simply delete duplicates or if the polygon gets messed up
        val geometryFiltered = ArrayList<Vector3D>()
        geometry.forEach { point ->
            if (!geometryFiltered.any { it.equalsVector(point)}) {
                geometryFiltered.add(point)
            }
        }
        // add the first point as last point to close the loop
        geometryFiltered.add(geometryFiltered.first())

        return geometryFiltered
    }

    /**
     * Resolves geometry of [IfcOpenShell] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcOpenShell(entity: IfcOpenShell): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // TODO implement
        return geometry
    }

    /**
     * Resolves geometry of [IfcFace] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFace(entity: IfcFace): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // resolve list of bounds
        entity.bounds.forEach { bound ->
            // note: for now only handle IfcFaceOuterBound to ignore cutouts, ...
            // ... there is only one IfcFaceOuterBound defined for IfcFaceBound ...
            // ... so this iteration should only return one object
            if (bound is IfcFaceOuterBound) {
                geometry.addAll(resolveIfcFaceBound(bound))
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcFaceBound] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFaceBound(entity: IfcFaceBound): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // handle IfcFaceOuterBound and IfcFaceBound the same way
        val data = resolveLoop(entity.bound)
        // handle orientation, if true no action needed, if false handle translation
        if (entity.orientation.value == 1) {
            // if sense is FALSE the senses of all its component oriented edges are implicitly reversed  when used in the face
            // TODO handle orientation, translate data
        }
        geometry.addAll(data)
        return geometry
    }

    /**
     * Resolves geometry of [IfcLoop] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveLoop(entity: IfcLoop): List<Vector3D> {
        when (entity) {
            is IfcPolyLoop -> {
                return resolvePolyLoop(entity)
            }
            is IfcVertexLoop -> {
                // TODO implement
            }
            is IfcEdgeLoop -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcPolyLoop] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolvePolyLoop(entity: IfcPolyLoop): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // collect all points of polygon loop
        entity.polygon.forEach { cartesian ->
            // TODO handle axis, refDirection
            val x = cartesian.coordinates[0]
            val y = cartesian.coordinates[1]
            geometry.add(Vector3D(x, y, 0.0))
        }
        // add first coordinate again to close the loop
        geometry.add(Vector3D(geometry[0].x, geometry[0].y, geometry[0].z))
        return geometry
    }

    /**
     * Resolves geometry of [IfcGeometricRepresentationItem] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcGeometricRepresentationItem(entity: IfcGeometricRepresentationItem): List<Vector3D> {
        when (entity) {
            is IfcCompositeCurveSegment -> {
                // TODO implement
            }
            is IfcCurve -> {
                return resolveIfcCurve(entity)
            }
            is IfcDirection -> {
                // TODO implement
            }
            is IfcPlacement -> {
                // TODO implement
            }
            is IfcPoint -> {
                // TODO implement
            }
            is IfcSurface -> {
                // TODO implement
            }
            is IfcVector -> {
                // TODO implement
            }
            is IfcBooleanResult -> {
                return resolveIfcBooleanResult(entity)
            }
            is IfcSolidModel -> {
                return resolveIfcSolidModel(entity)
            }
            is IfcHalfSpaceSolid -> {
                return resolveIfcHalfSpaceSolid(entity)
            }
            is IfcBoundingBox -> {
                return resolveIfcBoundingBox(entity)
            }
            is IfcCartesianTransformationOperator -> {
                // TODO implement
            }
            is IfcSectionedSpine -> {
                // TODO implement
            }
            is IfcGeometricSet -> {
                // TODO implement
            }
            is IfcFaceBasedSurfaceModel -> {
                // TODO implement
            }
            is IfcShellBasedSurfaceModel -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcBoundingBox] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBoundingBox(entity: IfcBoundingBox): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        val cartesianPoint = entity.corner.coordinates

        // TODO handle axis and refDirection

        geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
        geometry.add(Vector3D(cartesianPoint[0] + entity.xDim, cartesianPoint[1], 0.0))
        geometry.add(Vector3D(cartesianPoint[0] + entity.xDim, cartesianPoint[1] + entity.yDim, 0.0))
        geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1] + entity.yDim, 0.0))
        geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
        return geometry
    }

    /**
     * Resolves geometry of [IfcCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCurve(entity: IfcCurve): List<Vector3D> {
        when (entity) {
            is IfcBoundedCurve -> {
                return resolveIfcBoundedCurve(entity)
            }
            is IfcConic -> {
                return resolveIfcConic(entity)
            }
            is IfcLine -> {
                // TODO implement
            }
            is IfcOffsetCurve2D -> {
                // TODO implement
            }
            is IfcOffsetCurve3D -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcBoundedCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBoundedCurve(entity: IfcBoundedCurve): List<Vector3D> {
        when (entity) {
            is IfcCompositeCurve -> {
                return resolveIfcCompositeCurve(entity)
            }
            is IfcPolyline -> {
                return resolveIfcPolyline(entity)
            }
            is IfcTrimmedCurve -> {
                return resolveIfcTrimmedCurve(entity)
            }
            is IfcBSplineCurve -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcConic] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcConic(entity: IfcConic): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity) {
            is IfcCircle -> {
                // get center
                val center = (entity.position as IfcAxis2Placement2D).location.coordinates
                // TODO handle axis and refDirection

                // add 37 points to representing the circle
                for (i in 0..360 step 10) {
                    val x = center[0] + entity.radius * cos(Math.toRadians(i.toDouble()))
                    val y = center[0] + entity.radius * sin(Math.toRadians(i.toDouble()))
                    geometry.add(Vector3D(x, y, 0.0))
                }
                geometry.add(geometry[0])
                return geometry
            }
            is IfcEllipse -> {
                // TODO implement
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcPolyline] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcPolyline(entity: IfcPolyline): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        entity.points.forEach { point ->
            // TODO handle axis and refDirection
            geometry.add(Vector3D(point.coordinates[0], point.coordinates[1], 0.0))
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcCompositeCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCompositeCurve(entity: IfcCompositeCurve): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        entity.segments.forEach { segment ->
            val parentCurve = segment.parentCurve
            val parentCurveShape = resolveIfcCurve(parentCurve)
            when (segment.transition) {
                // TODO need for connection between segments?
                IfcTransitionCode.DISCONTINUOUS -> {
                    // open curve
                    // TODO implement
                }
                IfcTransitionCode.CONTINUOUS -> {
                    // TODO implement
                }
                IfcTransitionCode.CONTSAMEGRADIENT -> {
                    // TODO implement
                }
                IfcTransitionCode.CONTSAMEGRADIENTSAMECURVATURE -> {
                    // TODO implement
                }
            }
            if (segment.sameSense == Tristate.TRUE) {
                // TODO implement
            } else {
                // TODO implement
            }
//            if (parentCurveShape.isEmpty()) {
//                return ArrayList()    // return empty array once one segment cannot be resolved
//            }
            geometry.addAll(parentCurveShape)
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcTrimmedCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcTrimmedCurve(entity: IfcTrimmedCurve): List<Vector3D> {

        // TODO test/fix

        val basisCurveGeometry = resolveIfcCurve(entity.basisCurve)
        val trimmingPoint1 = entity.trim1[1] as IfcCartesianPoint
        val trimmingPoint2 = entity.trim2[1] as IfcCartesianPoint

        // TODO handle axis and refDirection


        // find trimming points in basic curve
        var deltaTrimmingPoint1 = 10000.0
        var indexClosestToTrimmingPoint1 = 0
        var deltaTrimmingPoint2 = 10000.0
        var indexClosestToTrimmingPoint2 = 0
        var i = 0
        basisCurveGeometry.forEach { point ->
            var deltaX = point.x - trimmingPoint1.coordinates[0]
            var deltaY = point.y - trimmingPoint1.coordinates[1]
            var delta = sqrt(deltaX.pow(2.0) + deltaY.pow(2.0))
            if (delta < deltaTrimmingPoint1) {
                deltaTrimmingPoint1 = delta
                indexClosestToTrimmingPoint1 = i
            }
            deltaX = point.x - trimmingPoint2.coordinates[0]
            deltaY = point.y - trimmingPoint2.coordinates[1]
            delta = sqrt(deltaX.pow(2.0) + deltaY.pow(2.0))
            if (delta < deltaTrimmingPoint2) {
                deltaTrimmingPoint2 = delta
                indexClosestToTrimmingPoint2 = i
            }
            ++i
        }

        // trim
        if (entity.senseAgreement == Tristate.TRUE) {
            // trim counterclockwise
            return basisCurveGeometry.slice(indexClosestToTrimmingPoint2..indexClosestToTrimmingPoint1)
        } else {
            // trim clockwise
            val trimmed = ArrayList<Vector3D>()
            trimmed.addAll(basisCurveGeometry.slice(indexClosestToTrimmingPoint1 until basisCurveGeometry.size))
            trimmed.addAll(basisCurveGeometry.slice(0 .. indexClosestToTrimmingPoint2))
            return trimmed
        }
    }

    /**
     * Resolves geometry of [IfcSolidModel] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcSolidModel(entity: IfcSolidModel): List<Vector3D> {
        when (entity) {
            is IfcManifoldSolidBrep -> {
                return resolveIfcManifoldSolidBrep(entity)
            }
            is IfcSweptAreaSolid -> {
                return resolveIfcSweptAreaSolid(entity)
            }
            is IfcCsgSolid -> {
                // TODO implement
            }
            is IfcSweptDiskSolid -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcSweptAreaSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcSweptAreaSolid(entity: IfcSweptAreaSolid): List<Vector3D> {
        when (entity) {
            is IfcExtrudedAreaSolid -> {
                return resolveIfcExtrudedAreaSolid(entity)
            }
            is IfcRevolvedAreaSolid -> {
                // TODO Implement
            }
            is IfcSurfaceCurveSweptAreaSolid -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcExtrudedAreaSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcExtrudedAreaSolid(entity: IfcExtrudedAreaSolid): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        var locationX = entity.position.location.coordinates[0]
        var locationY = entity.position.location.coordinates[1]

        when (entity.sweptArea) {
            // TODO pack subclasses in methods

            // handle IfcParameterizedProfileDef
            is IfcIShapeProfileDef -> {/*TODO implement */
            }
            is IfcLShapeProfileDef -> {/*TODO implement */
            }
            is IfcUShapeProfileDef -> {/*TODO implement */
            }
            is IfcCShapeProfileDef -> {/*TODO implement */
            }
            is IfcZShapeProfileDef -> {/*TODO implement */
            }
            is IfcTShapeProfileDef -> {/*TODO implement */
            }
            is IfcCraneRailFShapeProfileDef -> {/*TODO implement */
            }
            is IfcCraneRailAShapeProfileDef -> {/*TODO implement */
            }
            is IfcCircleProfileDef -> {/*TODO implement */
            }
            is IfcEllipseProfileDef -> {/*TODO implement */
            }
            is IfcRectangleProfileDef -> {
                locationX = (entity.sweptArea as IfcRectangleProfileDef).position.location.coordinates[0]
                locationY = (entity.sweptArea as IfcRectangleProfileDef).position.location.coordinates[1]

                // TODO handle axis and refDirection

                // extract dimensions
                val halfXDim = (entity.sweptArea as IfcRectangleProfileDef).xDim / 2.0
                val halfYDim = (entity.sweptArea as IfcRectangleProfileDef).yDim / 2.0

                // get points of rectangle shape
                geometry.add(Vector3D(locationX - halfXDim, locationY - halfYDim, 0.0))
                geometry.add(Vector3D(locationX + halfXDim, locationY - halfYDim, 0.0))
                geometry.add(Vector3D(locationX + halfXDim, locationY + halfYDim, 0.0))
                geometry.add(Vector3D(locationX - halfXDim, locationY + halfYDim, 0.0))
                geometry.add(Vector3D(locationX - halfXDim, locationY - halfYDim, 0.0))

                // get points at depth
                if (entity.extrudedDirection.directionRatios[0] == 0.0 && entity.extrudedDirection.directionRatios[1] == 0.0) {
                    // depth vector equals z-axis, so we can ignore points at depth because in 2D the points will
                    // simply be duplicated
                    return geometry
                } else {
                    // TODO add points at depth
                }

                return geometry
            }
            is IfcTrapeziumProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryOpenProfileDef
            is IfcCenterLineProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryClosedProfileDef
            is IfcArbitraryClosedProfileDef -> {
                // TODO handle Ifc2x3tc1_IfcArbitraryProfileDefWithVoids

                val outerCurve = (entity.sweptArea as IfcArbitraryClosedProfileDef).outerCurve
                val curveShape = resolveIfcCurve(outerCurve)
                curveShape.forEach { point ->
                    point.x += locationX
                    point.y += locationY
                }
                geometry.addAll(curveShape)

                // get points at depth
                if (entity.extrudedDirection.directionRatios[0] == 0.0 && entity.extrudedDirection.directionRatios[1] == 0.0) {
                    // depth vector equals z-axis, so we can ignore points at depth because in 2D the points will
                    // simply be duplicated
                    return geometry
                } else {
                    // TODO add points at depth
                }

                return geometry
            }
            // handle IfcCompositeProfileDef
            is IfcCompositeProfileDef -> {/*TODO implement */
            }
            // handle IfcDerivedProfileDef
            is IfcDerivedProfileDef -> {/*TODO implement */
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcManifoldSolidBrep] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcManifoldSolidBrep(entity: IfcManifoldSolidBrep): List<Vector3D> {
        when (entity) {
            is IfcFacetedBrep -> {
                return resolveIfcFacetedBrep(entity)
            }
            is IfcFacetedBrepWithVoids -> {
                // TODO Implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcFacetedBrep] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFacetedBrep(entity: IfcFacetedBrep): List<Vector3D> {
        // resolve attribute outer: IfcClosedShell subtype of IfcConnectedFaceSet
        return resolveIfcTopologicalRepresentationItem(entity.outer)
    }

    /**
     * Resolves geometry of [IfcHalfSpaceSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcHalfSpaceSolid(entity: IfcHalfSpaceSolid): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity) {
            is IfcBoxedHalfSpace -> {
                // TODO implement
            }
            is IfcPolygonalBoundedHalfSpace -> {
                // TODO Implement
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcCsgPrimitive3D] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCsgPrimitive3D(entity: IfcCsgPrimitive3D): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity) {
            is IfcBlock -> {
                // TODO implement
            }
            is IfcRectangularPyramid -> {
                // TODO Implement
            }
            is IfcRightCircularCylinder -> {
                // TODO implement
            }
            is IfcRightCircularCone -> {
                // TODO implement
            }
            is IfcSphere -> {
                // TODO implement
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [IfcBooleanResult] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBooleanResult(entity: IfcBooleanResult): List<Vector3D> {
        // TODO handle IfcBooleanClippingResult

        val geometryFirstOperand = resolveIfcBooleanOperand(entity.firstOperand)
        val geometrySecondOperand = resolveIfcBooleanOperand(entity.secondOperand)
        when (entity.operator.value) {
            IfcBooleanOperator.DIFFERENCE.value -> {
                // points which are in the first operand, but not in the second operand
                val pointsInFirstOperandOnly = ArrayList<Vector3D>()
                // TODO FIX: check if point of second operator in first operator
                geometryFirstOperand.forEach { point ->
                    if(!Geometry.isInsidePolygon(point, geometrySecondOperand)){
                        pointsInFirstOperandOnly.add(point)
                    }
                }
                return pointsInFirstOperandOnly
            }
            IfcBooleanOperator.INTERSECTION.value -> {
                // TODO implement
            }
            IfcBooleanOperator.UNION.value -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [IfcBooleanOperand] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBooleanOperand(entity: IfcBooleanOperand): List<Vector3D> {
        when (entity) {
            is IfcSolidModel -> {
                return resolveIfcSolidModel(entity)
            }
            is IfcHalfSpaceSolid -> {
                return resolveIfcHalfSpaceSolid(entity)
            }
            is IfcBooleanResult -> {
                return resolveIfcBooleanResult(entity)
            }
            is IfcCsgPrimitive3D -> {
                return resolveIfcCsgPrimitive3D(entity)
            }
        }
        return ArrayList()
    }

}

/**
 * Class keeps information about IfcProduct
 * @param productRepresentation IfcProductionRepresentation of IfcProduct
 * @param representation IfcRepresentation of [productRepresentation] (BoundingBox, Body, Axis)
 * @param type of product
 */
data class Ifc2x3tc1RepresentationInfo(
    val productRepresentation: IfcProductRepresentation,
    val representation: IfcRepresentation,
    val type: SupportedObjectType
)