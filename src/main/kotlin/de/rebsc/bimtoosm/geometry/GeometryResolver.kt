package de.rebsc.bimtoosm.geometry

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
import org.bimserver.models.ifc2x3tc1.IfcRepresentation as Ifc2x3tc1_IfcRepresentation
import org.bimserver.models.ifc2x3tc1.IfcProductRepresentation as Ifc2x3tc1_IfcProductRepresentation

class GeometryResolver(private val solution: GeometrySolution) {

    private val logger = Logger.get(this::class.java)

    /**
     * Geometry cache for ifc4 objects
     */
    var geometryCacheIfc4: MutableMap<Ifc4RepresentationPair, List<Vector3D>> = HashMap()

    /**
     * Geometry cache for ifc2x3tc1 objects
     */
    var geometryCacheIfc2x3tc1: MutableMap<Ifc2x3tc1RepresentationPair, List<Vector3D>> = HashMap()

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc4]
     * @param productRepresentation of wall object
     */
    fun resolveWall(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            var itemProcessed = false
            productRepresentation.representations.forEach { rep ->
                if (solution == GeometrySolution.BOUNDING_BOX && rep.representationIdentifier.equals("Box", true)) {
                    geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                    itemProcessed = true
                    return@forEach
                }
                if (solution == GeometrySolution.BODY && rep.representationIdentifier.equals("Body", true)) {
                    logger.info("${GeometrySolution.BODY::class.java.name} not supported right now")
                    // TODO implement
                    itemProcessed = true
                }
            }

            // TODO if applied solution not available use default: Bounding Box
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    fun resolveWall(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveSlab(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            var itemProcessed = false
            productRepresentation.representations.forEach { rep ->
                if (solution == GeometrySolution.BOUNDING_BOX && rep.representationIdentifier.equals("Box", true)) {
                    geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                    itemProcessed = true
                    return@forEach
                }
                if (solution == GeometrySolution.BODY && rep.representationIdentifier.equals("Body", true)) {
                    logger.info("${GeometrySolution.BODY::class.java.name} not supported right now")
                    // TODO implement
                    itemProcessed = true
                }
            }

            // TODO if applied solution not available use default: Bounding Box
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    fun resolveSlab(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveColumn(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            var itemProcessed = false
            productRepresentation.representations.forEach { rep ->
                if (solution == GeometrySolution.BOUNDING_BOX && rep.representationIdentifier.equals("Box", true)) {
                    geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                    itemProcessed = true
                    return@forEach
                }
                if (solution == GeometrySolution.BODY && rep.representationIdentifier.equals("Body", true)) {
                    logger.info("${GeometrySolution.BODY::class.java.name} not supported right now")
                    // TODO implement
                    itemProcessed = true
                }
            }

            // TODO if applied solution not available use default: Bounding Box
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    fun resolveColumn(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveDoor(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            var itemProcessed = false
            productRepresentation.representations.forEach { rep ->
                if (solution == GeometrySolution.BOUNDING_BOX && rep.representationIdentifier.equals("Box", true)) {
                    geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                    itemProcessed = true
                    return@forEach
                }
                if (solution == GeometrySolution.BODY && rep.representationIdentifier.equals("Body", true)) {
                    logger.info("${GeometrySolution.BODY::class.java.name} not supported right now")
                    // TODO implement
                    itemProcessed = true
                }
            }

            // TODO if applied solution not available use default: Bounding Box
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    fun resolveDoor(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveStair(productRepresentation: Ifc4_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc4_IfcProductDefinitionShape) {
            var itemProcessed = false
            productRepresentation.representations.forEach { rep ->
                if (solution == GeometrySolution.BOUNDING_BOX && rep.representationIdentifier.equals("Box", true)) {
                    geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                    itemProcessed = true
                    return@forEach
                }
                if (solution == GeometrySolution.BODY && rep.representationIdentifier.equals("Body", true)) {
                    logger.info("${GeometrySolution.BODY::class.java.name} not supported right now")
                    // TODO implement
                    itemProcessed = true
                }
            }

            // TODO if applied solution not available use default: Bounding Box
            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc4_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc4_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    fun resolveStair(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
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
                    // disable z-dimension
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

        return geometry
    }

}

data class Ifc4RepresentationPair(
    val productRepresentation: Ifc4_IfcProductRepresentation,
    val representation: Ifc4_IfcRepresentation
)

data class Ifc2x3tc1RepresentationPair(
    val productRepresentation: Ifc2x3tc1_IfcProductRepresentation,
    val representation: Ifc2x3tc1_IfcRepresentation
)