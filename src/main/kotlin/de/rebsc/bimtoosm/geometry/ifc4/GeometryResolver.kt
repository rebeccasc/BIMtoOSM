package de.rebsc.bimtoosm.geometry.ifc4

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
import org.bimserver.models.ifc4.IfcBoundingBox as Ifc4_IfcBoundingBox
import org.bimserver.models.ifc4.IfcMappedItem as Ifc4_IfcMappedItem
import org.bimserver.models.ifc4.IfcStyledItem as Ifc4_IfcStyledItem
import org.bimserver.models.ifc4.IfcGeometricRepresentationItem as Ifc4_IfcGeometricRepresentationItem
import org.bimserver.models.ifc4.IfcMaterialDefinitionRepresentation as Ifc4_IfcMaterialDefinitionRepresentation
import org.bimserver.models.ifc4.IfcProductDefinitionShape as Ifc4_IfcProductDefinitionShape
import org.bimserver.models.ifc4.IfcProductRepresentation as Ifc4_IfcProductRepresentation
import org.bimserver.models.ifc4.IfcRepresentation as Ifc4_IfcRepresentation
import org.bimserver.models.ifc4.IfcExtrudedAreaSolid as Ifc4_IfcExtrudedAreaSolid
import org.bimserver.models.ifc4.IfcFacetedBrep as Ifc4_IfcFacetedBrep
import org.bimserver.models.ifc4.IfcCurve as Ifc4_IfcCurve
import org.bimserver.models.ifc4.IfcPolyline as Ifc4_IfcPolyline
import org.bimserver.models.ifc4.IfcCompositeCurve as Ifc4_IfcCompositeCurve
import org.bimserver.models.ifc4.IfcTrimmedCurve as Ifc4_IfcTrimmedCurve
import org.bimserver.models.ifc4.IfcRectangleProfileDef as Ifc4_IfcRectangleProfileDef
import org.bimserver.models.ifc4.IfcIShapeProfileDef as Ifc4_IfcIShapeProfileDef
import org.bimserver.models.ifc4.IfcLShapeProfileDef as Ifc4_IfcLShapeProfileDef
import org.bimserver.models.ifc4.IfcUShapeProfileDef as Ifc4_IfcUShapeProfileDef
import org.bimserver.models.ifc4.IfcCShapeProfileDef as Ifc4_IfcCShapeProfileDef
import org.bimserver.models.ifc4.IfcZShapeProfileDef as Ifc4_IfcZShapeProfileDef
import org.bimserver.models.ifc4.IfcTShapeProfileDef as Ifc4_IfcTShapeProfileDef
import org.bimserver.models.ifc4.IfcCircleProfileDef as Ifc4_IfcCircleProfileDef
import org.bimserver.models.ifc4.IfcEllipseProfileDef as Ifc4_IfcEllipseProfileDef
import org.bimserver.models.ifc4.IfcTrapeziumProfileDef as Ifc4_IfcTrapeziumProfileDef
import org.bimserver.models.ifc4.IfcCenterLineProfileDef as Ifc4_IfcCenterLineProfileDef
import org.bimserver.models.ifc4.IfcArbitraryClosedProfileDef as Ifc4_IfcArbitraryClosedProfileDef
import org.bimserver.models.ifc4.IfcArbitraryProfileDefWithVoids as Ifc4_IfcArbitraryProfileDefWithVoids
import org.bimserver.models.ifc4.IfcCompositeProfileDef as Ifc4_IfcCompositeProfileDef
import org.bimserver.models.ifc4.IfcDerivedProfileDef as Ifc4_IfcDerivedProfileDef


class GeometryResolver(private val solution: GeometrySolution) {

    private val logger = Logger.get(this::class.java)

    /**
     * Geometry cache for ifc4 objects
     */
    var geometryCacheIfc4: MutableMap<Ifc4RepresentationInfo, List<Vector3D>> = HashMap()


    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of wall object
     */
    fun resolveWall(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_WALL)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of slab object
     */
    fun resolveSlab(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_SLAB)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of column object
     */
    fun resolveColumn(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_COLUMN)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of door object
     */
    fun resolveDoor(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_DOOR)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of window object
     */
    fun resolveWindow(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_WINDOW)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of stair object
     */
    fun resolveStair(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            val itemProcessed = resolveGeometry(productRepresentation, SupportedObjectType.IFC_STAIR)
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
        }
        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    // Utils

    private fun resolveGeometry(
        productRepresentation: Ifc4_IfcProductDefinitionShape,
        type: SupportedObjectType
    ): Boolean {
        val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis
        if (solution == GeometrySolution.BODY && repIndices.second != -1) {
            // body
            val rep = productRepresentation.representations[repIndices.second]
            geometryCacheIfc4[Ifc4RepresentationInfo(productRepresentation, rep, type)] = resolveBody(rep)
            return true
        }
        if (repIndices.first != -1) {
            // box
            val rep = productRepresentation.representations[repIndices.first]
            geometryCacheIfc4[Ifc4RepresentationInfo(productRepresentation, rep, type)] = resolveBoundingBox(rep)
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
     * Gets indices of representation types of [entity] in following order: box, body, axis.
     * If not available set to default value (-1)
     * @param entity to get representation type indices of
     * @return triple with indices for box, body, axis representation
     */
    private fun getRepresentationsIndices(entity: Ifc4_IfcProductDefinitionShape): Triple<Int, Int, Int> {
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
    private fun resolveBody(shapeRepresentation: Ifc4_IfcRepresentation): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        shapeRepresentation.items.forEach { item ->
            if (item is Ifc4_IfcExtrudedAreaSolid) {
                geometry.addAll(resolveIfcExtrudedAreaSolid(item))
                return@forEach
            }
            if (item is Ifc4_IfcFacetedBrep) {
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
    private fun resolveBoundingBox(shapeRepresentation: Ifc4_IfcRepresentation): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        shapeRepresentation.items.forEach { item ->
            if (item is Ifc4_IfcGeometricRepresentationItem) {
                if (item is Ifc4_IfcBoundingBox) {
                    val cartesianPoint = item.corner.coordinates
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
                    geometry.add(Vector3D(cartesianPoint[0] + item.xDim, cartesianPoint[1], 0.0))
                    geometry.add(Vector3D(cartesianPoint[0] + item.xDim, cartesianPoint[1] + item.yDim, 0.0))
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1] + item.yDim, 0.0))
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
                }
                // INFO: ignore other IfcGeometricRepresentationItems for now
            }
            if (item is Ifc4_IfcMappedItem) {
                logger.info("${Ifc4_IfcMappedItem::class.java.name} not supported right now")
                // TODO implement
            }
            if (item is Ifc4_IfcStyledItem) {
                logger.info("${Ifc4_IfcStyledItem::class.java.name} not supported right now")
                // TODO implement
            }
        }

        if (geometry.isEmpty()) {
            logger.warn("resolveBoundingBox-> Resolved geometry of ${shapeRepresentation.expressId} is empty")
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc4_IfcExtrudedAreaSolid] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcExtrudedAreaSolid(entity: Ifc4_IfcExtrudedAreaSolid): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        var locationX = entity.position.location.coordinates[0]
        var locationY = entity.position.location.coordinates[1]

        when (entity.sweptArea) {
            // handle IfcParameterizedProfileDef
            is Ifc4_IfcIShapeProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcLShapeProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcUShapeProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcCShapeProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcZShapeProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcTShapeProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcCircleProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcEllipseProfileDef -> {/*TODO implement */
            }
            is Ifc4_IfcRectangleProfileDef -> {
                locationX = (entity.sweptArea as Ifc4_IfcRectangleProfileDef).position.location.coordinates[0]
                locationY = (entity.sweptArea as Ifc4_IfcRectangleProfileDef).position.location.coordinates[1]

                // extract dimensions
                val halfxDim = (entity.sweptArea as Ifc4_IfcRectangleProfileDef).xDim / 2.0
                val halfyDim = (entity.sweptArea as Ifc4_IfcRectangleProfileDef).yDim / 2.0
                // get points of shape
                geometry.add(Vector3D(locationX - halfxDim, locationY - halfyDim, 0.0))
                geometry.add(Vector3D(locationX + halfxDim, locationY - halfyDim, 0.0))
                geometry.add(Vector3D(locationX + halfxDim, locationY + halfyDim, 0.0))
                geometry.add(Vector3D(locationX - halfxDim, locationY + halfyDim, 0.0))
                geometry.add(Vector3D(locationX - halfxDim, locationY - halfyDim, 0.0))
                return geometry
            }
            is Ifc4_IfcTrapeziumProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryOpenProfileDef
            is Ifc4_IfcCenterLineProfileDef -> {/*TODO implement */
            }
            // handle IfcArbitraryClosedProfileDef
            is Ifc4_IfcArbitraryClosedProfileDef -> {
                val outerCurve = (entity.sweptArea as Ifc4_IfcArbitraryClosedProfileDef).outerCurve
                val curveShape = resolveIfcCurve(outerCurve)
                curveShape.forEach { point ->
                    point.x += locationX
                    point.y += locationY
                }
                geometry.addAll(curveShape)
                return geometry
            }
            is Ifc4_IfcArbitraryProfileDefWithVoids -> {/*TODO implement */
            }
            // handle IfcCompositeProfileDef
            is Ifc4_IfcCompositeProfileDef -> {/*TODO implement */
            }
            // handle IfcDerivedProfileDef
            is Ifc4_IfcDerivedProfileDef -> {/*TODO implement */
            }
            // TODO check for more possible types
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc4_IfcFacetedBrep] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcFacetedBrep(entity: Ifc4_IfcFacetedBrep): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // TODO implement
        return geometry
    }

    /**
     * Resolves geometry of [Ifc4_IfcCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCurve(entity: Ifc4_IfcCurve): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        when (entity.eClass().name) {
            // handle subtypes of IfcBoundedCurve
            "IfcBSplineCurve" -> {/*TODO implement*/
            }
            "IfcIndexedPolyCurve" -> {
                geometry.addAll(resolveIfcCompositeCurve(entity as Ifc4_IfcCompositeCurve))
            }
            "IfcPolyline" -> {
                geometry.addAll(resolveIfcPolyline(entity as Ifc4_IfcPolyline))
            }
            "IfcCompositeCurve" -> {
                geometry.addAll(resolveIfcCompositeCurve(entity as Ifc4_IfcCompositeCurve))
            }
            "IfcTrimmedCurve" -> {
                geometry.addAll(resolveIfcTrimmedCurve(entity as Ifc4_IfcTrimmedCurve))
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
            // handle IfcPcurve
            "IfcPcurve" -> {/*TODO implement*/
            }

        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc4_IfcPolyline] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcPolyline(entity: Ifc4_IfcPolyline): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        entity.points.forEach { point ->
            geometry.add(Vector3D(point.coordinates[0], point.coordinates[1], 0.0))
        }
        return geometry
    }

    /**
     * Resolves geometry of [Ifc4_IfcCompositeCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcCompositeCurve(entity: Ifc4_IfcCompositeCurve): List<Vector3D> {
        val geometry = ArrayList<Vector3D>()
        // TODO implement
        return geometry
    }

    /**
     * Resolves geometry of [Ifc4_IfcTrimmedCurve] entity
     * @param entity to resolve geometry
     * @return List holding resolved local coordinates
     */
    private fun resolveIfcTrimmedCurve(entity: Ifc4_IfcTrimmedCurve): List<Vector3D> {
        // TODO implement
        return ArrayList()
    }

}

/**
 * Class keeps information about IfcProduct
 * @param productRepresentation IfcProductionRepresentation of IfcProduct
 * @param representation IfcRepresentation of [productRepresentation] (BoundingBox, Body, Axis)
 * @param type of product
 */
data class Ifc4RepresentationInfo(
    val productRepresentation: Ifc4_IfcProductRepresentation,
    val representation: Ifc4_IfcRepresentation,
    val type: SupportedObjectType
)