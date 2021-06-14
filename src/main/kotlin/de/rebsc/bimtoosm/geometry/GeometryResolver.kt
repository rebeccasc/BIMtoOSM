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
import org.bimserver.models.ifc2x3tc1.IfcBoundingBox as Ifc2x3tc1_IfcBoundingBox
import org.bimserver.models.ifc2x3tc1.IfcMappedItem as Ifc2x3tc1_IfcMappedItem
import org.bimserver.models.ifc2x3tc1.IfcStyledItem as Ifc2x3tc1_IfcStyledItem
import org.bimserver.models.ifc2x3tc1.IfcGeometricRepresentationItem as Ifc2x3tc1_IfcGeometricRepresentationItem
import org.bimserver.models.ifc2x3tc1.IfcRepresentation as Ifc2x3tc1_IfcRepresentation
import org.bimserver.models.ifc2x3tc1.IfcProductDefinitionShape as Ifc2x3tc1_IfcProductDefinitionShape
import org.bimserver.models.ifc2x3tc1.IfcProductRepresentation as Ifc2x3tc1_IfcProductRepresentation
import org.bimserver.models.ifc2x3tc1.IfcMaterialDefinitionRepresentation as Ifc2x3tc1_IfcMaterialDefinitionRepresentation


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
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

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

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of wall object
     */
    fun resolveWall(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationPair(productRepresentation, rep)] =
                    resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
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
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

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

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of slab object
     */
    fun resolveSlab(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationPair(productRepresentation, rep)] =
                    resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
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
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

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

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of column object
     */
    fun resolveColumn(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationPair(productRepresentation, rep)] =
                    resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
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
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

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

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of door object
     */
    fun resolveDoor(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationPair(productRepresentation, rep)] =
                    resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
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
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

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

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of window object
     */
    fun resolveWindow(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationPair(productRepresentation, rep)] =
                    resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
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
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc4[Ifc4RepresentationPair(productRepresentation, rep)] = resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

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

    /**
     * Resolves object geometry and adds it to [geometryCacheIfc2x3tc1]
     * @param productRepresentation of stair object
     */
    fun resolveStair(productRepresentation: Ifc2x3tc1_IfcProductRepresentation) {
        // shape representation
        if (productRepresentation is Ifc2x3tc1_IfcProductDefinitionShape) {
            var itemProcessed = false
            val repIndices = getRepresentationsIndices(productRepresentation) // box, body, axis

            // get geometry
            if (solution == GeometrySolution.BODY && repIndices.second != -1) {
                // body
                val rep = productRepresentation.representations[repIndices.second]
                // TODO implement
                itemProcessed = true
            } else if (repIndices.first != -1) {
                // box
                val rep = productRepresentation.representations[repIndices.first]
                geometryCacheIfc2x3tc1[Ifc2x3tc1RepresentationPair(productRepresentation, rep)] =
                    resolveBoundingBox(rep)
                itemProcessed = true
            } else if (repIndices.third != -1) {
                // axis
                val rep = productRepresentation.representations[repIndices.third]
                // TODO implement
                itemProcessed = true
            }

            if (!itemProcessed) {
                throw BIMtoOSMException("No valid IfcShapeRepresentation found for ${productRepresentation.expressId}")
            }
            return
        }

        // material representation
        if (productRepresentation is Ifc2x3tc1_IfcMaterialDefinitionRepresentation) {
            logger.info("${Ifc2x3tc1_IfcMaterialDefinitionRepresentation::class.java.name} not supported right now")
            // TODO implement
        }
    }

    /**
     * Gets indices of representation types of [entity] in following order: boy, body, axis.
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
                    // disable z-dimension
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
                    geometry.add(Vector3D(cartesianPoint[0] + item.xDim, cartesianPoint[1], 0.0))
                    geometry.add(Vector3D(cartesianPoint[0] + item.xDim, cartesianPoint[1] + item.yDim, 0.0))
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1] + item.yDim, 0.0))
                    geometry.add(Vector3D(cartesianPoint[0], cartesianPoint[1], 0.0))
                }
                // INFO: ignore other IfcGeometricRepresentationItems for now
            }
            if (item is Ifc2x3tc1_IfcMappedItem) {
                logger.info("${Ifc4_IfcMappedItem::class.java.name} not supported right now")
                // TODO implement
            }
            if (item is Ifc2x3tc1_IfcStyledItem) {
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