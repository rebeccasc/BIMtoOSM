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
import de.rebsc.bimtoosm.utils.math.Vector3D
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
            if (item is Ifc2x3tc1_IfcExtrudedAreaSolid) {
                geometry.addAll(resolveIfcExtrudedAreaSolid(item))
                return@forEach
            }
            if (item is Ifc2x3tc1_IfcFacetedBrep) {
                geometry.addAll(resolveIfcFacetedBrep(item))
                return@forEach
            }
            // TODO handle more body representation items
            logger.warn("resolveBody -> Unknown RepresentationType of ${shapeRepresentation.expressId}")
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
            if (item is Ifc2x3tc1_IfcGeometricRepresentationItem) {
                if (item is Ifc2x3tc1_IfcBoundingBox) {
                    val cartesianPoint = item.corner.coordinates
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
                    geometry.add(Vector3D(cartesianPoint[0] + item.xDim, cartesianPoint[1], 0.0))
                    geometry.add(Vector3D(cartesianPoint[0] + item.xDim, cartesianPoint[1] + item.yDim, 0.0))
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1] + item.yDim, 0.0))
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
                }
                // INFO: ignore other IfcGeometricRepresentationItems for now
            }
            if (item is Ifc2x3tc1_IfcMappedItem) {
                logger.info("${Ifc2x3tc1_IfcMappedItem::class.java.name} not supported right now")
                // TODO implement
            }
            if (item is Ifc2x3tc1_IfcStyledItem) {
                logger.info("${Ifc2x3tc1_IfcStyledItem::class.java.name} not supported right now")
                // TODO implement
            }
        }

        if (geometry.isEmpty()) {
            logger.warn("resolveBoundingBox-> Resolved geometry of ${shapeRepresentation.expressId} is empty")
        }
        return geometry
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
                // TODO test new placement
                locationX = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).position.location.coordinates[0]
                locationY = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).position.location.coordinates[1]

                // extract dimensions
                val halfxDim = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).xDim / 2.0
                val halfyDim = (entity.sweptArea as Ifc2x3tc1_IfcRectangleProfileDef).yDim / 2.0
                // get points of shape
                geometry.add(Vector3D(locationX - halfxDim, locationY - halfyDim, 0.0))
                geometry.add(Vector3D(locationX + halfxDim, locationY - halfyDim, 0.0))
                geometry.add(Vector3D(locationX + halfxDim, locationY + halfyDim, 0.0))
                geometry.add(Vector3D(locationX - halfxDim, locationY + halfyDim, 0.0))
                geometry.add(Vector3D(locationX - halfxDim, locationY - halfyDim, 0.0))
                return geometry
            }
            is Ifc2x3tc1_IfcTrapeziumProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryOpenProfileDef
            is Ifc2x3tc1_IfcCenterLineProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryClosedProfileDef
            is Ifc2x3tc1_IfcArbitraryClosedProfileDef -> {
                val outerCurve = (entity.sweptArea as Ifc2x3tc1_IfcArbitraryClosedProfileDef).outerCurve
                val curveShape = resolveIfcCurve(outerCurve)
                curveShape.forEach { point ->
                    point.x += locationX
                    point.y += locationY
                }
                geometry.addAll(curveShape)
                return geometry
            }
            is Ifc2x3tc1_IfcArbitraryProfileDefWithVoids -> {/*TODO implement */
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
     * Resolves geometry of [Ifc2x3tc1_IfcFacetedBrep] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFacetedBrep(entity: Ifc2x3tc1_IfcFacetedBrep): List<Vector3D> {
        // resolve attribute outer: IfcClosedShell subtype of IfcConnectedFaceSet
        return resolveIfcTopologicalRepresentationItem(entity.outer)
    }

    /**
     * TODO add description
     */
    private fun resolveIfcTopologicalRepresentationItem(entity: Ifc2x3tc1_IfcTopologicalRepresentationItem): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
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
        return geometry
    }

    /**
     * TODO add description
     */
    private fun resolveIfcConnectedFaceSet(entity: Ifc2x3tc1_IfcConnectedFaceSet): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // check if closed shell or open shell
        when (entity) {
            is Ifc2x3tc1_IfcClosedShell -> {
                return resolveIfcClosedShell(entity)
            }
            is Ifc2x3tc1_IfcOpenShell -> {
                return resolveIfcOpenShell(entity)
            }
        }
        return geometry
    }

    /**
     * TODO add description
     */
    private fun resolveIfcClosedShell(entity: Ifc2x3tc1_IfcClosedShell): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // resolve list of CfsFaces
        entity.cfsFaces.forEach { face ->
            geometry.addAll(resolveIfcFace(face))
            // TODO handle point duplications
        }
        return geometry
    }

    /**
     * TODO add description
     */
    private fun resolveIfcOpenShell(entity: Ifc2x3tc1_IfcOpenShell): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // TODO implement
        return geometry
    }

    /**
     * TODO add description
     */
    private fun resolveIfcFace(entity: Ifc2x3tc1_IfcFace): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // resolve list of bounds
        entity.bounds.forEach { bound ->
            // note:
            // for now only handle IfcFaceOuterBound to ignore cutouts,
            // there is only one IfcFaceOuterBound defined for IfcFaceBound so this loop should only be called once
            if (bound is Ifc2x3tc1_IfcFaceOuterBound) {
                geometry.addAll(resolveIfcFaceBound(bound))
            }
        }
        return geometry
    }

    /**
     * TODO add description
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
     * TODO add description
     */
    private fun resolveLoop(entity: Ifc2x3tc1_IfcLoop): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
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
        return geometry
    }

    /**
     * TODO add description
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
     * Resolves geometry of [Ifc2x3tc1_IfcCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCurve(entity: Ifc2x3tc1_IfcCurve): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity.eClass().name) {
            // TODO use 'is' to check class?
            // handle subtypes of IfcBoundedCurve
            "IfcCompositeCurve" -> {
                geometry.addAll(resolveIfcCompositeCurve(entity as Ifc2x3tc1_IfcCompositeCurve))
            }
            "IfcPolyline" -> {
                geometry.addAll(resolveIfcPolyline(entity as Ifc2x3tc1_IfcPolyline))
            }
            "IfcTrimmedCurve" -> {
                geometry.addAll(resolveIfcTrimmedCurve(entity as Ifc2x3tc1_IfcTrimmedCurve))
            }
            "IfcBSplineCurve" -> {/*TODO implement*/
            }
            // handle subtypes of IfcConic
            "IfcCircle" -> {/*TODO implement*/
            }
            "IfcEllipse" -> {/*TODO implement*/
            }
            // handle IfcLine
            "IfcLine" -> {/*TODO implement*/
            }
            // handle IfcOffsetCurve2D/3D
            "IfcOffsetCurve2D" -> {/*TODO implement*/
            }
            "IfcOffsetCurve3D" -> {/*TODO implement*/
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
            if (parentCurveShape.isEmpty()) {
                return ArrayList()    // return empty array once one segment cannot be resolved
            }
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
        // TODO implement properly - handle trim of basis curve
        return resolveIfcCurve(entity.basisCurve)
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