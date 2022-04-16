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
import org.bimserver.models.ifc2x3tc1.IfcTransitionCode
import org.bimserver.models.ifc2x3tc1.Tristate
import org.bimserver.models.ifc4.IfcBSplineCurve
import org.bimserver.models.ifc4.IfcBoundingBox
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement2D as Ifc2x3tc1_IfcAxis2Placement2D
import org.bimserver.models.ifc2x3tc1.IfcAxis2Placement3D as Ifc2x3tc1_IfcAxis2Placement3D
import org.bimserver.models.ifc2x3tc1.IfcBoundingBox as Ifc2x3tc1_IfcBoundingBox
import org.bimserver.models.ifc2x3tc1.IfcMappedItem as Ifc2x3tc1_IfcMappedItem
import org.bimserver.models.ifc2x3tc1.IfcStyledItem as Ifc2x3tc1_IfcStyledItem
import org.bimserver.models.ifc2x3tc1.IfcGeometricRepresentationItem as Ifc2x3tc1_IfcGeometricRepresentationItem
import org.bimserver.models.ifc2x3tc1.IfcRepresentation as Ifc2x3tc1_IfcRepresentation
import org.bimserver.models.ifc2x3tc1.IfcProductDefinitionShape as Ifc2x3tc1_IfcProductDefinitionShape
import org.bimserver.models.ifc2x3tc1.IfcProductRepresentation as Ifc2x3tc1_IfcProductRepresentation
import org.bimserver.models.ifc2x3tc1.IfcMaterialDefinitionRepresentation as Ifc2x3tc1_IfcMaterialDefinitionRepresentation
import org.bimserver.models.ifc2x3tc1.IfcExtrudedAreaSolid as Ifc2x3tc1_IfcExtrudedAreaSolid
import org.bimserver.models.ifc2x3tc1.IfcFacetedBrep as Ifc2x3tc1_IfcFacetedBrep
import org.bimserver.models.ifc2x3tc1.IfcFacetedBrepWithVoids as Ifc2x3tc1_IfcFacetedBrepWithVoids
import org.bimserver.models.ifc2x3tc1.IfcRectangleProfileDef as Ifc2x3tc1_IfcRectangleProfileDef
import org.bimserver.models.ifc2x3tc1.IfcIShapeProfileDef as Ifc2x3tc1_IfcIShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcLShapeProfileDef as Ifc2x3tc1_IfcLShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcUShapeProfileDef as Ifc2x3tc1_IfcUShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcCShapeProfileDef as Ifc2x3tc1_IfcCShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcZShapeProfileDef as Ifc2x3tc1_IfcZShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcTShapeProfileDef as Ifc2x3tc1_IfcTShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcCraneRailFShapeProfileDef as Ifc2x3tc1_IfcCraneRailFShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcCraneRailAShapeProfileDef as Ifc2x3tc1_IfcCraneRailAShapeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcCircleProfileDef as Ifc2x3tc1_IfcCircleProfileDef
import org.bimserver.models.ifc2x3tc1.IfcEllipseProfileDef as Ifc2x3tc1_IfcEllipseProfileDef
import org.bimserver.models.ifc2x3tc1.IfcTrapeziumProfileDef as Ifc2x3tc1_IfcTrapeziumProfileDef
import org.bimserver.models.ifc2x3tc1.IfcCenterLineProfileDef as Ifc2x3tc1_IfcCenterLineProfileDef
import org.bimserver.models.ifc2x3tc1.IfcArbitraryClosedProfileDef as Ifc2x3tc1_IfcArbitraryClosedProfileDef
import org.bimserver.models.ifc2x3tc1.IfcArbitraryProfileDefWithVoids as Ifc2x3tc1_IfcArbitraryProfileDefWithVoids
import org.bimserver.models.ifc2x3tc1.IfcCompositeProfileDef as Ifc2x3tc1_IfcCompositeProfileDef
import org.bimserver.models.ifc2x3tc1.IfcDerivedProfileDef as Ifc2x3tc1_IfcDerivedProfileDef
import org.bimserver.models.ifc2x3tc1.IfcCurve as Ifc2x3tc1_IfcCurve
import org.bimserver.models.ifc2x3tc1.IfcPolyline as Ifc2x3tc1_IfcPolyline
import org.bimserver.models.ifc2x3tc1.IfcCompositeCurve as Ifc2x3tc1_IfcCompositeCurve
import org.bimserver.models.ifc2x3tc1.IfcCompositeCurveSegment as Ifc2x3tc1_IfcCompositeCurveSegment
import org.bimserver.models.ifc2x3tc1.IfcTrimmedCurve as Ifc2x3tc1_IfcTrimmedCurve
import org.bimserver.models.ifc2x3tc1.IfcConnectedFaceSet as Ifc2x3tc1_IfcConnectedFaceSet
import org.bimserver.models.ifc2x3tc1.IfcClosedShell as Ifc2x3tc1_IfcClosedShell
import org.bimserver.models.ifc2x3tc1.IfcOpenShell as Ifc2x3tc1_IfcOpenShell
import org.bimserver.models.ifc2x3tc1.IfcTopologicalRepresentationItem as Ifc2x3tc1_IfcTopologicalRepresentationItem
import org.bimserver.models.ifc2x3tc1.IfcFace as Ifc2x3tc1_IfcFace
import org.bimserver.models.ifc2x3tc1.IfcEdge as Ifc2x3tc1_IfcEdge
import org.bimserver.models.ifc2x3tc1.IfcFaceBound as Ifc2x3tc1_IfcFaceBound
import org.bimserver.models.ifc2x3tc1.IfcFaceOuterBound as Ifc2x3tc1_IfcFaceOuterBound
import org.bimserver.models.ifc2x3tc1.IfcPath as Ifc2x3tc1_IfcPath
import org.bimserver.models.ifc2x3tc1.IfcVertex as Ifc2x3tc1_IfcVertex
import org.bimserver.models.ifc2x3tc1.IfcLoop as Ifc2x3tc1_IfcLoop
import org.bimserver.models.ifc2x3tc1.IfcPolyLoop as Ifc2x3tc1_IfcPolyLoop
import org.bimserver.models.ifc2x3tc1.IfcVertexLoop as Ifc2x3tc1_IfcVertexLoop
import org.bimserver.models.ifc2x3tc1.IfcEdgeLoop as Ifc2x3tc1_IfcEdgeLoop
import org.bimserver.models.ifc2x3tc1.IfcCircle as Ifc2x3tc1_IfcCircle
import org.bimserver.models.ifc2x3tc1.IfcEllipse as Ifc2x3tc1_IfcEllipse
import org.bimserver.models.ifc2x3tc1.IfcLine as Ifc2x3tc1_IfcLine
import org.bimserver.models.ifc2x3tc1.IfcOffsetCurve2D as Ifc2x3tc1_IfcOffsetCurve2D
import org.bimserver.models.ifc2x3tc1.IfcOffsetCurve3D as Ifc2x3tc1_IfcOffsetCurve3D
import org.bimserver.models.ifc2x3tc1.IfcBooleanResult as Ifc2x3tc1_IfcBooleanResult
import org.bimserver.models.ifc2x3tc1.IfcBooleanOperator as Ifc2x3tc1_IfcBooleanOperator
import org.bimserver.models.ifc2x3tc1.IfcBooleanOperand as Ifc2x3tc1_IfcBooleanOperand
import org.bimserver.models.ifc2x3tc1.IfcSolidModel as Ifc2x3tc1_IfcSolidModel
import org.bimserver.models.ifc2x3tc1.IfcManifoldSolidBrep as Ifc2x3tc1_IfcManifoldSolidBrep
import org.bimserver.models.ifc2x3tc1.IfcSweptAreaSolid as Ifc2x3tc1_IfcSweptAreaSolid
import org.bimserver.models.ifc2x3tc1.IfcCsgSolid as Ifc2x3tc1_IfcCsgSolid
import org.bimserver.models.ifc2x3tc1.IfcSweptDiskSolid as Ifc2x3tc1_IfcSweptDiskSolid
import org.bimserver.models.ifc2x3tc1.IfcHalfSpaceSolid as Ifc2x3tc1_IfcHalfSpaceSolid
import org.bimserver.models.ifc2x3tc1.IfcBoxedHalfSpace as Ifc2x3tc1_IfcBoxedHalfSpace
import org.bimserver.models.ifc2x3tc1.IfcPolygonalBoundedHalfSpace as Ifc2x3tc1_IfcPolygonalBoundedHalfSpace
import org.bimserver.models.ifc2x3tc1.IfcCsgPrimitive3D as Ifc2x3tc1_IfcCsgPrimitive3D
import org.bimserver.models.ifc2x3tc1.IfcBlock as Ifc2x3tc1_IfcBlock
import org.bimserver.models.ifc2x3tc1.IfcRectangularPyramid as Ifc2x3tc1_IfcRectangularPyramid
import org.bimserver.models.ifc2x3tc1.IfcRightCircularCylinder as Ifc2x3tc1_IfcRightCircularCylinder
import org.bimserver.models.ifc2x3tc1.IfcRightCircularCone as Ifc2x3tc1_IfcRightCircularCone
import org.bimserver.models.ifc2x3tc1.IfcSphere as Ifc2x3tc1_IfcSphere
import org.bimserver.models.ifc2x3tc1.IfcRevolvedAreaSolid as Ifc2x3tc1_IfcRevolvedAreaSolid
import org.bimserver.models.ifc2x3tc1.IfcSurfaceCurveSweptAreaSolid as Ifc2x3tc1_IfcSurfaceCurveSweptAreaSolid
import org.bimserver.models.ifc2x3tc1.IfcDirection as Ifc2x3tc1_IfcDirection
import org.bimserver.models.ifc2x3tc1.IfcPlacement as Ifc2x3tc1_IfcPlacement
import org.bimserver.models.ifc2x3tc1.IfcSurface as Ifc2x3tc1_IfcSurface
import org.bimserver.models.ifc2x3tc1.IfcPoint as Ifc2x3tc1_IfcPoint
import org.bimserver.models.ifc2x3tc1.IfcVector as Ifc2x3tc1_IfcVector
import org.bimserver.models.ifc2x3tc1.IfcCartesianTransformationOperator as Ifc2x3tc1_IfcCartesianTransformationOperator
import org.bimserver.models.ifc2x3tc1.IfcSectionedSpine as Ifc2x3tc1_IfcSectionedSpine
import org.bimserver.models.ifc2x3tc1.IfcGeometricSet as Ifc2x3tc1_IfcGeometricSet
import org.bimserver.models.ifc2x3tc1.IfcFaceBasedSurfaceModel as Ifc2x3tc1_IfcFaceBasedSurfaceModel
import org.bimserver.models.ifc2x3tc1.IfcShellBasedSurfaceModel as Ifc2x3tc1_IfcShellBasedSurfaceModel
import org.bimserver.models.ifc2x3tc1.IfcBoundedCurve as Ifc2x3tc1_IfcBoundedCurve
import org.bimserver.models.ifc2x3tc1.IfcConic as Ifc2x3tc1_IfcConic
import org.bimserver.models.ifc2x3tc1.IfcCartesianPoint as Ifc2x3tc1_IfcCartesianPoint


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
    fun resolveWall(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_WALL)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of slab object
     */
    fun resolveSlab(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_SLAB)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of column object
     */
    fun resolveColumn(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_COLUMN)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of door object
     */
    fun resolveDoor(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_DOOR)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of window object
     */
    fun resolveWindow(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_WINDOW)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of stair object
     */
    fun resolveStair(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_STAIR)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    // Utils

    /**
     * TODO add description
     */
    private fun resolveGeometry(
        productRepresentation: Ifc2x3tc1_IfcProductDefinitionShape,
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
    private fun getRepresentationsIndices(entity: Ifc2x3tc1_IfcProductRepresentation): Triple<Int, Int, Int> {
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
    private fun resolveBody(shapeRepresentation: Ifc2x3tc1_IfcRepresentation): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        shapeRepresentation.items.forEach { item ->
            when (item) {
                is Ifc2x3tc1_IfcTopologicalRepresentationItem -> {
                    geometry.addAll(resolveIfcTopologicalRepresentationItem(item))
                }
                is Ifc2x3tc1_IfcGeometricRepresentationItem -> {
                    geometry.addAll(resolveIfcGeometricRepresentationItem(item))
                }
                is Ifc2x3tc1_IfcMappedItem -> {
                    // TODO implement
                }
                is Ifc2x3tc1_IfcStyledItem -> {
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
    private fun resolveBoundingBox(shapeRepresentation: Ifc2x3tc1_IfcRepresentation): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        shapeRepresentation.items.forEach { item ->
            when (item) {
                is Ifc2x3tc1_IfcGeometricRepresentationItem -> {
                    geometry.addAll(resolveIfcGeometricRepresentationItem(item))
                }
                is Ifc2x3tc1_IfcMappedItem -> {
                    // TODO implement
                }
                is Ifc2x3tc1_IfcStyledItem -> {
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
     * Resolves geometry of [Ifc2x3tc1_IfcTopologicalRepresentationItem] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcTopologicalRepresentationItem(entity: Ifc2x3tc1_IfcTopologicalRepresentationItem): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcConnectedFaceSet -> {
                return resolveIfcConnectedFaceSet(entity)
            }
            is Ifc2x3tc1_IfcEdge -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcFace -> {
                return resolveIfcFace(entity)
            }
            is Ifc2x3tc1_IfcFaceBound -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcPath -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcVertex -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcLoop -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcConnectedFaceSet] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcConnectedFaceSet(entity: Ifc2x3tc1_IfcConnectedFaceSet): List<Vector3D> {
        // check if closed shell or open shell
        when (entity) {
            is Ifc2x3tc1_IfcClosedShell -> {
                return resolveIfcClosedShell(entity)
            }
            is Ifc2x3tc1_IfcOpenShell -> {
                return resolveIfcOpenShell(entity)
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcClosedShell] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcClosedShell(entity: Ifc2x3tc1_IfcClosedShell): List<Vector3D> {
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
            if (!geometryFiltered.any { it.x == point.x && it.y == point.y && it.z == point.z }) {
                geometryFiltered.add(point)
            }
        }
        // add the first point as last point to close the loop
//        geometryFiltered.add(geometryFiltered.first())
        return geometryFiltered
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcOpenShell] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcOpenShell(entity: Ifc2x3tc1_IfcOpenShell): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // TODO implement
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcFace] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFace(entity: Ifc2x3tc1_IfcFace): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // resolve list of bounds
        entity.bounds.forEach { bound ->
            // note:
            // for now only handle IfcFaceOuterBound to ignore cutouts,
            // there is only one IfcFaceOuterBound defined for IfcFaceBound so this iteration should only return one object
            if (bound is Ifc2x3tc1_IfcFaceOuterBound) {
                geometry.addAll(resolveIfcFaceBound(bound))
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcFaceBound] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFaceBound(entity: Ifc2x3tc1_IfcFaceBound): List<Vector3D> {
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
     * Resolves geometry of [Ifc2x3tc1_IfcLoop] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveLoop(entity: Ifc2x3tc1_IfcLoop): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcPolyLoop -> {
                return resolvePolyLoop(entity)
            }
            is Ifc2x3tc1_IfcVertexLoop -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcEdgeLoop -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcPolyLoop] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolvePolyLoop(entity: Ifc2x3tc1_IfcPolyLoop): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // collect all points of polygon loop
        entity.polygon.forEach { cartesian ->
            val x = cartesian.coordinates[0]
            val y = cartesian.coordinates[1]
            geometry.add(Vector3D(x, y, 0.0))
        }
        // add first coordinate again to close the loop
        geometry.add(Vector3D(geometry[0].x, geometry[0].y, geometry[0].z))
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcGeometricRepresentationItem] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcGeometricRepresentationItem(entity: Ifc2x3tc1_IfcGeometricRepresentationItem): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcCompositeCurveSegment -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcCurve -> {
                return resolveIfcCurve(entity)
            }
            is Ifc2x3tc1_IfcDirection -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcPlacement -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcPoint -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcSurface -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcVector -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcBooleanResult -> {
                return resolveIfcBooleanResult(entity)
            }
            is Ifc2x3tc1_IfcSolidModel -> {
                return resolveIfcSolidModel(entity)
            }
            is Ifc2x3tc1_IfcHalfSpaceSolid -> {
                return resolveIfcHalfSpaceSolid(entity)
            }
            is IfcBoundingBox -> {
                return resolveIfcBoundingBox(entity as org.bimserver.models.ifc2x3tc1.IfcBoundingBox)
            }
            is Ifc2x3tc1_IfcCartesianTransformationOperator -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcSectionedSpine -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcGeometricSet -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcFaceBasedSurfaceModel -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcShellBasedSurfaceModel -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcBoundingBox] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBoundingBox(entity: Ifc2x3tc1_IfcBoundingBox): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        val cartesianPoint = entity.corner.coordinates
        geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
        geometry.add(Vector3D(cartesianPoint[0] + entity.xDim, cartesianPoint[1], 0.0))
        geometry.add(Vector3D(cartesianPoint[0] + entity.xDim, cartesianPoint[1] + entity.yDim, 0.0))
        geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1] + entity.yDim, 0.0))
        geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCurve(entity: Ifc2x3tc1_IfcCurve): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcBoundedCurve -> {
                return resolveIfcBoundedCurve(entity)
            }
            is Ifc2x3tc1_IfcConic -> {
                return resolveIfcConic(entity)
            }
            is Ifc2x3tc1_IfcLine -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcOffsetCurve2D -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcOffsetCurve3D -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcBoundedCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBoundedCurve(entity: Ifc2x3tc1_IfcBoundedCurve): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcCompositeCurve -> {
                return resolveIfcCompositeCurve(entity)
            }
            is Ifc2x3tc1_IfcPolyline -> {
                return resolveIfcPolyline(entity)
            }
            is Ifc2x3tc1_IfcTrimmedCurve -> {
                return resolveIfcTrimmedCurve(entity)
            }
            is IfcBSplineCurve -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcConic] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcConic(entity: Ifc2x3tc1_IfcConic): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity) {
            is Ifc2x3tc1_IfcCircle -> {
                // get center
                val center = (entity.position as org.bimserver.models.ifc2x3tc1.IfcAxis2Placement2D).location.coordinates
                // TODO handle axis and refDirection

                // add 37 points to represent circle
                for (i in 0..360 step 10) {
                    val x = center[0] + entity.radius * cos(Math.toRadians(i.toDouble()))
                    val y = center[0] + entity.radius * sin(Math.toRadians(i.toDouble()))
                    geometry.add(Vector3D(x, y, 0.0))
                }
                geometry.add(geometry[0])
                return geometry
            }
            is Ifc2x3tc1_IfcEllipse -> {
                // TODO implement
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcPolyline] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcPolyline(entity: Ifc2x3tc1_IfcPolyline): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        entity.points.forEach { point ->
            // TODo handle axis and refDirection
            geometry.add(Vector3D(point.coordinates[0], point.coordinates[1], 0.0))
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcCompositeCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCompositeCurve(entity: Ifc2x3tc1_IfcCompositeCurve): List<Vector3D> {
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
     * Resolves geometry of [Ifc2x3tc1_IfcTrimmedCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcTrimmedCurve(entity: Ifc2x3tc1_IfcTrimmedCurve): List<Vector3D> {

        // TODO test/fix

        val basisCurveGeometry = resolveIfcCurve(entity.basisCurve)
        val trimmingPoint1 = entity.trim1[1] as Ifc2x3tc1_IfcCartesianPoint
        val trimmingPoint2 = entity.trim2[1] as Ifc2x3tc1_IfcCartesianPoint

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
     * Resolves geometry of [Ifc2x3tc1_IfcSolidModel] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcSolidModel(entity: Ifc2x3tc1_IfcSolidModel): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcManifoldSolidBrep -> {
                return resolveIfcManifoldSolidBrep(entity)
            }
            is Ifc2x3tc1_IfcSweptAreaSolid -> {
                return resolveIfcSweptAreaSolid(entity)
            }
            is Ifc2x3tc1_IfcCsgSolid -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcSweptDiskSolid -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcSweptAreaSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcSweptAreaSolid(entity: Ifc2x3tc1_IfcSweptAreaSolid): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcExtrudedAreaSolid -> {
                return resolveIfcExtrudedAreaSolid(entity)
            }
            is Ifc2x3tc1_IfcRevolvedAreaSolid -> {
                // TODO Implement
            }
            is Ifc2x3tc1_IfcSurfaceCurveSweptAreaSolid -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcExtrudedAreaSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcExtrudedAreaSolid(entity: Ifc2x3tc1_IfcExtrudedAreaSolid): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        var locationX = entity.position.location.coordinates[0]
        var locationY = entity.position.location.coordinates[1]

        when (entity.sweptArea) {
            // TODO pack subclasses in methods

            // handle IfcParameterizedProfileDef
            is Ifc2x3tc1_IfcIShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcLShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcUShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcCShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcZShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcTShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcCraneRailFShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcCraneRailAShapeProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcCircleProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcEllipseProfileDef -> {/*TODO implement */
            }
            is Ifc2x3tc1_IfcRectangleProfileDef -> {
                locationX = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).position.location.coordinates[0]
                locationY = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).position.location.coordinates[1]

                // TODO handle axis and refDirection

                // extract dimensions
                val halfXDim = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).xDim / 2.0
                val halfYDim = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).yDim / 2.0

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
            is Ifc2x3tc1_IfcTrapeziumProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryOpenProfileDef
            is Ifc2x3tc1_IfcCenterLineProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryClosedProfileDef
            is Ifc2x3tc1_IfcArbitraryClosedProfileDef -> {
                // TODO handle Ifc2x3tc1_IfcArbitraryProfileDefWithVoids

                val outerCurve = (entity.sweptArea as Ifc2x3tc1_IfcArbitraryClosedProfileDef).outerCurve
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
            is Ifc2x3tc1_IfcCompositeProfileDef -> {/*TODO implement */
            }
            // handle IfcDerivedProfileDef
            is Ifc2x3tc1_IfcDerivedProfileDef -> {/*TODO implement */
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcManifoldSolidBrep] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcManifoldSolidBrep(entity: Ifc2x3tc1_IfcManifoldSolidBrep): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcFacetedBrep -> {
                return resolveIfcFacetedBrep(entity)
            }
            is Ifc2x3tc1_IfcFacetedBrepWithVoids -> {
                // TODO Implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcFacetedBrep] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFacetedBrep(entity: Ifc2x3tc1_IfcFacetedBrep): List<Vector3D> {
        // resolve attribute outer: IfcClosedShell subtype of IfcConnectedFaceSet
        return resolveIfcTopologicalRepresentationItem(entity.outer)
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcHalfSpaceSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcHalfSpaceSolid(entity: Ifc2x3tc1_IfcHalfSpaceSolid): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity) {
            is Ifc2x3tc1_IfcBoxedHalfSpace -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcPolygonalBoundedHalfSpace -> {
                // TODO Implement
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcCsgPrimitive3D] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCsgPrimitive3D(entity: Ifc2x3tc1_IfcCsgPrimitive3D): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity) {
            is Ifc2x3tc1_IfcBlock -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcRectangularPyramid -> {
                // TODO Implement
            }
            is Ifc2x3tc1_IfcRightCircularCylinder -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcRightCircularCone -> {
                // TODO implement
            }
            is Ifc2x3tc1_IfcSphere -> {
                // TODO implement
            }
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcBooleanResult] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBooleanResult(entity: Ifc2x3tc1_IfcBooleanResult): List<Vector3D> {
        // TODO handle IfcBooleanClippingResult

        val geometryFirstOperand = resolveIfcBooleanOperand(entity.firstOperand)
        val geometrySecondOperand = resolveIfcBooleanOperand(entity.secondOperand)
        when (entity.operator.value) {
            Ifc2x3tc1_IfcBooleanOperator.DIFFERENCE.value -> {
                // points which are in the first operand, but not in the second operand
                val pointsInFirstOperandOnly = ArrayList<Vector3D>()
                // TODO Jordan test if point of second operator in first operator
                geometryFirstOperand.forEach { point ->
                    if(!Geometry.isInsidePolygon(point, geometrySecondOperand)){
                        pointsInFirstOperandOnly.add(point)
                    }
                }
                return pointsInFirstOperandOnly
            }
            Ifc2x3tc1_IfcBooleanOperator.INTERSECTION.value -> {
                // TODO implement
            }
            Ifc2x3tc1_IfcBooleanOperator.UNION.value -> {
                // TODO implement
            }
        }
        return ArrayList()
    }

    /**
     * Resolves geometry of [Ifc2x3tc1_IfcBooleanOperand] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcBooleanOperand(entity: Ifc2x3tc1_IfcBooleanOperand): List<Vector3D> {
        when (entity) {
            is Ifc2x3tc1_IfcSolidModel -> {
                return resolveIfcSolidModel(entity)
            }
            is Ifc2x3tc1_IfcHalfSpaceSolid -> {
                return resolveIfcHalfSpaceSolid(entity)
            }
            is Ifc2x3tc1_IfcBooleanResult -> {
                return resolveIfcBooleanResult(entity)
            }
            is Ifc2x3tc1_IfcCsgPrimitive3D -> {
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
    val productRepresentation: Ifc2x3tc1_IfcProductRepresentation,
    val representation: Ifc2x3tc1_IfcRepresentation,
    val type: SupportedObjectType
)