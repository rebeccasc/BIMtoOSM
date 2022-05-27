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

import de.rebsc.bimtoosm.data.osm.*
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3PlacementResolver
import de.rebsc.bimtoosm.geometry.ifc4.Ifc4GeometryResolver
import de.rebsc.bimtoosm.geometry.ifc4.Ifc4PlacementResolver
import de.rebsc.bimtoosm.logger.Logger
import de.rebsc.bimtoosm.parser.IfcUnitPrefix
import de.rebsc.bimtoosm.utils.IdGenerator
import de.rebsc.bimtoosm.utils.UnitConverter
import de.rebsc.bimtoosm.utils.math.Point2D
import de.rebsc.bimtoosm.utils.math.Point3D
import org.bimserver.emf.IfcModelInterface
import org.bimserver.emf.Schema
import org.bimserver.models.ifc2x3tc1.IfcBuildingStorey as Ifc2x3tc1_IfcBuildingStorey
import org.bimserver.models.ifc2x3tc1.IfcProduct
import org.bimserver.models.ifc2x3tc1.IfcProductRepresentation
import org.bimserver.models.ifc2x3tc1.IfcStair as Ifc2x3tc1_IfcStair
import org.bimserver.models.ifc2x3tc1.IfcWindow as Ifc2x3tc1_IfcWindow
import org.bimserver.models.ifc2x3tc1.IfcSpatialStructureElement as Ifc2x3tc1_IfcSpatialStructureElement
import org.bimserver.models.ifc2x3tc1.IfcColumn as Ifc2x3tc1_IfcColumn
import org.bimserver.models.ifc2x3tc1.IfcDoor as Ifc2x3tc1_IfcDoor
import org.bimserver.models.ifc2x3tc1.IfcSlab as Ifc2x3tc1_IfcSlab
import org.bimserver.models.ifc2x3tc1.IfcSlabTypeEnum as Ifc2x3tc1_IfcSlabTypeEnum
import org.bimserver.models.ifc2x3tc1.IfcSpace as Ifc2x3tc1_IfcSpace
import org.bimserver.models.ifc2x3tc1.IfcWall as Ifc2x3tc1_IfcWall
import org.bimserver.models.ifc4.IfcColumn as Ifc4_IfcColumn
import org.bimserver.models.ifc4.IfcDoor as Ifc4_IfcDoor
import org.bimserver.models.ifc4.IfcSlab as Ifc4_IfcSlab
import org.bimserver.models.ifc4.IfcSlabTypeEnum as Ifc4_IfcSlabTypeEnum
import org.bimserver.models.ifc4.IfcStair as Ifc4_IfcStair
import org.bimserver.models.ifc4.IfcWall as Ifc4_IfcWall
import org.bimserver.models.ifc4.IfcWindow as Ifc4_IfcWindow

/**
 * Engine to extract the object geometry
 */
class GeometryEngine(private val solution: GeometrySolution) {

    private val logger = Logger.get(this::class.java)
    private lateinit var units: IfcUnitPrefix

    /**
     * Transform each object in [model] into OSM object and put it into [OSMDataSet]
     * @param model ifc model
     * @param units ifc unit prefix
     * @return [OSMDataSet]
     */
    fun transformToOSM(model: IfcModelInterface, units: IfcUnitPrefix): OSMDataSet {
        val schema = model.modelMetaData.ifcHeader.ifcSchemaVersion
        this.units = units

        // resolve placement and geometry of object separately
        val placementResolverIfc4 = Ifc4PlacementResolver()
        val geometryResolverIfc4 = Ifc4GeometryResolver(solution)

        val placementResolverIfc2x3tc1 = Ifc2x3PlacementResolver()
        val geometryResolverIfc2x3tc1 = Ifc2x3GeometryResolver(solution)

        // connect placement object with geometry object
        val connector: MutableMap<Long, Long> = HashMap()
        // osm data set
        val osmDataSet = OSMDataSet()

        if (schema == Schema.IFC4.headerName) {
            // extract geometry ifc4 data into geometry cache
            extractIfc4GeometryToCache(model, connector, placementResolverIfc4, geometryResolverIfc4)
            // transform geometry cache ifc4 to osm
            transformIfc4GeometryToOSM(model, connector, placementResolverIfc4, geometryResolverIfc4, osmDataSet)
        }
        if (schema == Schema.IFC2X3TC1.headerName) {
            // extract geometry ifc2x3tc1 data into geometry cache
            extractIfc2x3tc1GeometryToCache(model, connector, placementResolverIfc2x3tc1, geometryResolverIfc2x3tc1)
            // transform geometry ifc2x3tc1 cache to osm
            transformIfc2x3tc1GeometryToOSM(
                model, connector, placementResolverIfc2x3tc1, geometryResolverIfc2x3tc1, osmDataSet
            )
        }

        return osmDataSet
    }

    /**
     * Extract Ifc4 elements placement into [de.rebsc.bimtoosm.geometry.ifc4.Ifc4PlacementResolver] cache
     * and Ifc4 elements geometry into [de.rebsc.bimtoosm.geometry.ifc4.Ifc4GeometryResolver] cache
     * @param model with ifc data
     * @param connector connects placement object with geometry object
     * @param placementResolver resolves and keeps objects placement
     * @param geometryResolver resolves and keeps objects geometry
     */
    private fun extractIfc4GeometryToCache(
        model: IfcModelInterface,
        connector: MutableMap<Long, Long>,
        placementResolver: Ifc4PlacementResolver,
        geometryResolver: Ifc4GeometryResolver
    ) {
        model.getAllWithSubTypes(Ifc4_IfcWall::class.java).forEach { wall ->
            if (wall.representation == null) return@forEach
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver.resolvePlacement(wall.objectPlacement)
            geometryResolver.resolveWall(wall.representation)
        }
        model.getAllWithSubTypes(Ifc4_IfcSlab::class.java).forEach { slab ->
            if (slab.representation == null) return@forEach
            if (slab.predefinedType == Ifc4_IfcSlabTypeEnum.ROOF) return@forEach           // skip roofs
            connector[slab.objectPlacement.expressId] = slab.representation.expressId
            placementResolver.resolvePlacement(slab.objectPlacement)
            geometryResolver.resolveSlab(slab.representation)
        }
        model.getAllWithSubTypes(Ifc4_IfcColumn::class.java).forEach { column ->
            if (column.representation == null) return@forEach
            connector[column.objectPlacement.expressId] = column.representation.expressId
            placementResolver.resolvePlacement(column.objectPlacement)
            geometryResolver.resolveColumn(column.representation)
        }
        model.getAllWithSubTypes(Ifc4_IfcDoor::class.java).forEach { door ->
            if (door.representation == null) return@forEach
            connector[door.objectPlacement.expressId] = door.representation.expressId
            placementResolver.resolvePlacement(door.objectPlacement)
            geometryResolver.resolveDoor(door.representation)
        }
        model.getAllWithSubTypes(Ifc4_IfcWindow::class.java).forEach { window ->
            if (window.representation == null) return@forEach
            connector[window.objectPlacement.expressId] = window.representation.expressId
            placementResolver.resolvePlacement(window.objectPlacement)
            geometryResolver.resolveWindow(window.representation)
        }
        model.getAllWithSubTypes(Ifc4_IfcStair::class.java).forEach { stair ->
            if (stair.representation == null) return@forEach
            connector[stair.objectPlacement.expressId] = stair.representation.expressId
            placementResolver.resolvePlacement(stair.objectPlacement)
            geometryResolver.resolveStair(stair.representation)
        }
    }

    /**
     * Extract Ifc2x3tc1 elements placement into [de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3PlacementResolver] cache
     * and Ifc2x3tc1 elements geometry into [de.rebsc.bimtoosm.geometry.ifc2x3tc1.Ifc2x3GeometryResolver] cache
     * @param model with ifc data
     * @param connector connects placement object with geometry object
     * @param placementResolver resolves and keeps objects placement
     * @param geometryResolver resolves and keeps objects geometry
     */
    private fun extractIfc2x3tc1GeometryToCache(
        model: IfcModelInterface,
        connector: MutableMap<Long, Long>,
        placementResolver: Ifc2x3PlacementResolver,
        geometryResolver: Ifc2x3GeometryResolver
    ) {
        model.getAllWithSubTypes(Ifc2x3tc1_IfcWall::class.java).forEach { wall ->
            if (wall.representation == null) return@forEach
            connector[wall.objectPlacement.expressId] = wall.representation.expressId
            placementResolver.resolvePlacement(wall.objectPlacement)
            geometryResolver.resolveWall(wall.representation)
        }
        model.getAllWithSubTypes(Ifc2x3tc1_IfcSpace::class.java).forEach { space ->
            if (space.representation == null) return@forEach
            connector[space.objectPlacement.expressId] = space.representation.expressId
            placementResolver.resolvePlacement(space.objectPlacement)
            geometryResolver.resolveSpace(space.representation)
        }
        model.getAllWithSubTypes(Ifc2x3tc1_IfcSlab::class.java).forEach { slab ->
            if (slab.representation == null) return@forEach
            if (slab.predefinedType == Ifc2x3tc1_IfcSlabTypeEnum.ROOF) return@forEach      // skip roofs
            connector[slab.objectPlacement.expressId] = slab.representation.expressId
            placementResolver.resolvePlacement(slab.objectPlacement)
            geometryResolver.resolveSlab(slab.representation)
        }
        model.getAllWithSubTypes(Ifc2x3tc1_IfcColumn::class.java).forEach { column ->
            if (column.representation == null) return@forEach
            connector[column.objectPlacement.expressId] = column.representation.expressId
            placementResolver.resolvePlacement(column.objectPlacement)
            geometryResolver.resolveColumn(column.representation)
        }
        model.getAllWithSubTypes(Ifc2x3tc1_IfcDoor::class.java).forEach { door ->
            if (door.representation == null) return@forEach
            connector[door.objectPlacement.expressId] = door.representation.expressId
            placementResolver.resolvePlacement(door.objectPlacement)
            geometryResolver.resolveDoor(door.representation)
        }
        model.getAllWithSubTypes(Ifc2x3tc1_IfcWindow::class.java).forEach { window ->
            if (window.representation == null) return@forEach
            connector[window.objectPlacement.expressId] = window.representation.expressId
            placementResolver.resolvePlacement(window.objectPlacement)
            geometryResolver.resolveWindow(window.representation)
        }
        model.getAllWithSubTypes(Ifc2x3tc1_IfcStair::class.java).forEach { stair ->
            if (stair.representation == null) return@forEach
            connector[stair.objectPlacement.expressId] = stair.representation.expressId
            placementResolver.resolvePlacement(stair.objectPlacement)
            geometryResolver.resolveStair(stair.representation)
        }
    }

    /**
     * Transform extracted Ifc4 data into osm data.
     * Connect data from placement cache Ifc4 at [placementResolver] to geometry cache Ifc4
     * at [Ifc4GeometryResolver] to transform into node list. Tag nodes and add to [OSMDataSet]
     * @param model ifc model
     * @param connector connects placement object with geometry object
     * @param placementResolver resolves and keeps objects placement
     * @param geometryResolver resolves and keeps objects geometry
     * @param osmDataSet to add the data to
     */
    private fun transformIfc4GeometryToOSM(
        model: IfcModelInterface,
        connector: MutableMap<Long, Long>,
        placementResolver: Ifc4PlacementResolver,
        geometryResolver: Ifc4GeometryResolver,
        osmDataSet: OSMDataSet
    ) {
        geometryResolver.geometryCacheIfc4.forEach { representation ->
            // find placement connected to representation
            val connectorPlacements =
                connector.filterValues { it == representation.key.productRepresentation.expressId }
            val connectorPlacementKey = connectorPlacements.entries.first().key
            val placements = placementResolver.placementCacheIfc4.filterKeys { it.expressId == connectorPlacementKey }
            val placement = placements.entries.first()

            // transform representation using placement
            val osmNodeList = ArrayList<OSMNode>()
            representation.value.forEach { point ->
                val absolutePoint =
                    placementResolver.getAbsolutePoint(placement.value, Point3D(point.x, point.y, point.z))
                // TODO check points for unit (transform into meter)
                // TODO check if nodes need to be tagged
                // TODO add level tag
                val id = IdGenerator.createUUID(allowNegative = true)
                osmNodeList.add(OSMNode(id, Point2D(absolutePoint.x, absolutePoint.y)))
            }

            if (osmNodeList.isEmpty()) {
                logger.warn("Empty nodes list, skip representation")
                return@forEach
            }

            // tag and add to osm dataset
            osmDataSet.addNodes(osmNodeList)
            if (osmNodeList.size <= 1) {
                return@forEach
            }
            val id = IdGenerator.createUUID(allowNegative = true)
            // TODO add level tag
            val osmTagList = OSMTagCatalog.osmTagsFor(representation.key.type)
            val osmWay = OSMWay(id, osmNodeList, osmTagList)
            osmDataSet.addWay(osmWay)
        }

    }

    /**
     * Transform extracted Ifc2x3tc1 data into osm data.
     * Connect data from placement cache Ifc2x3tc1 at [placementResolver] to geometry cache Ifc2x3tc1
     * at [Ifc2x3GeometryResolver] to transform into node list. Tag nodes and add to [OSMDataSet]
     * @param model ifc model
     * @param connector connects placement object with geometry object
     * @param placementResolver resolves and keeps objects placement
     * @param geometryResolver resolves and keeps objects geometry
     * @param osmDataSet to add the data to
     */
    private fun transformIfc2x3tc1GeometryToOSM(
        model: IfcModelInterface,
        connector: MutableMap<Long, Long>,
        placementResolver: Ifc2x3PlacementResolver,
        geometryResolver: Ifc2x3GeometryResolver,
        osmDataSet: OSMDataSet
    ) {
        // get objects of each level
        val levelList = getLevelListIfc2x3tc1(model)

        // handle each object in cache
        geometryResolver.geometryCacheIfc2x3tc1.forEach { representation ->
            // find placement connected to representation
            val connectorPlacements =
                connector.filterValues { it == representation.key.productRepresentation.expressId }
            val connectorPlacementKey = connectorPlacements.entries.first().key
            val placements =
                placementResolver.placementCacheIfc2x3tc1.filterKeys { it.expressId == connectorPlacementKey }
            val placement = placements.entries.first()

            // find level tag of object
            val objectLevelTag = identifyLevelIfc2x3tc1(representation.key.productRepresentation, levelList)

            // transform representation to osm
            val osmNodeList = ArrayList<OSMNode>()
            representation.value.forEach { point ->
                // transform representation to proper placement
                var absolutePoint =
                    placementResolver.getAbsolutePoint(placement.value, Point3D(point.x, point.y, point.z))
                absolutePoint = UnitConverter.toMeter(absolutePoint, units.lengthUnitPrefix)

                // add tags
                val tagList = ArrayList<OSMTag>()
                if (objectLevelTag != null) {
                    tagList.add(OSMTag("level", "$objectLevelTag"))
                }
                // TODO add other tags
                val id = IdGenerator.createUUID(allowNegative = true)
                osmNodeList.add(OSMNode(id, Point2D(absolutePoint.x, absolutePoint.y), tagList))
            }

            if (osmNodeList.isEmpty()) {
                logger.warn("Empty nodes list, skip representation")
                return@forEach
            }

            // add nodes and way represented by nodes to osm data set
            osmDataSet.addNodes(osmNodeList)
            if (osmNodeList.size <= 1) {
                return@forEach
            }
            val id = IdGenerator.createUUID(allowNegative = true)
            // tag way
            val osmTagList = OSMTagCatalog.osmTagsFor(representation.key.type)
            if (objectLevelTag != null) {
                osmTagList.add(OSMTag("level", "$objectLevelTag"))
            }
            osmDataSet.addWay(OSMWay(id, osmNodeList, osmTagList))
        }
    }

    /**
     * TODO add description
     */
    private fun getLevelListIfc2x3tc1(model: IfcModelInterface): ArrayList<Pair<Double, ArrayList<IfcProduct>>> {
        var levelList = ArrayList<Pair<Double, ArrayList<IfcProduct>>>()
        model.getAllWithSubTypes(Ifc2x3tc1_IfcBuildingStorey::class.java).forEach { bs ->
            val level = ArrayList<IfcProduct>()
            traverseSpatialStructureIfc2x3tc1(bs, level)
            levelList.add(Pair(bs.elevation, level))
        }
        levelList = ArrayList(levelList.sortedWith(compareBy { it.first }))
        return levelList
    }

    /**
     * TODO add description
     */
    private fun identifyLevelIfc2x3tc1(
        representation: IfcProductRepresentation,
        levelList: ArrayList<Pair<Double, ArrayList<IfcProduct>>>
    ): Int? {
        levelList.forEach { levelPair ->
            levelPair.second.forEach { levelObj ->
                if (levelObj.representation == null) {
                    return@forEach
                }
                if (levelObj.representation.expressId == representation.expressId) {
                    return levelList.indexOf(levelPair)
                }
            }
        }
        return null
    }

    /**
     * TODO add description
     */
    private fun traverseSpatialStructureIfc2x3tc1(
        parent: Ifc2x3tc1_IfcSpatialStructureElement,
        objectList: ArrayList<IfcProduct>
    ) {
        // TODO fix this
        parent.containsElements.forEach { containment ->
            containment.relatedElements.forEach { product ->
                objectList.add(product)
            }
        }
        // get spaces
        parent.isDecomposedBy.forEach { aggregation ->
            aggregation.relatedObjects.forEach { child ->
                (child as Ifc2x3tc1_IfcSpace).containsElements.forEach { containment ->
                    containment.relatedElements.forEach { product ->
                        objectList.add(product)
                    }
                }
                traverseSpatialStructureIfc2x3tc1(child as Ifc2x3tc1_IfcSpatialStructureElement, objectList);
            }
        }
    }

}