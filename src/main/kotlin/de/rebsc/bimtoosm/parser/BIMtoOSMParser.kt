package de.rebsc.bimtoosm.parser

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

import de.rebsc.bimtoosm.api.BIMtoOSM
import de.rebsc.bimtoosm.data.OSMDataSet
import de.rebsc.bimtoosm.geometry.PlacementResolver
import de.rebsc.bimtoosm.loader.Loader
import de.rebsc.bimtoosm.logger.Logger
import de.rebsc.bimtoosm.optimizer.BIMFileOptimizer
import org.bimserver.emf.IfcModelInterface
import org.bimserver.emf.Schema
import org.bimserver.models.ifc4.IfcWall as Ifc4_IfcWall
import org.bimserver.models.ifc4.IfcSlab as Ifc4_IfcSlab
import org.bimserver.models.ifc4.IfcColumn as Ifc4_IfcColumn
import org.bimserver.models.ifc4.IfcDoor as Ifc4_IfcDoor
import org.bimserver.models.ifc4.IfcStair as Ifc4_IfcStair
import org.bimserver.models.ifc2x3tc1.IfcWall as Ifc2x3tc1_IfcWall
import org.bimserver.models.ifc2x3tc1.IfcSlab as Ifc2x3tc1_IfcSlab
import org.bimserver.models.ifc2x3tc1.IfcColumn as Ifc2x3tc1_IfcColumn
import org.bimserver.models.ifc2x3tc1.IfcDoor as Ifc2x3tc1_IfcDoor
import org.bimserver.models.ifc2x3tc1.IfcStair as Ifc2x3tc1_IfcStair
import kotlin.properties.Delegates


class BIMtoOSMParser(config: Configuration) : BIMtoOSM {

    /**
     * [BIMtoOSMParser] class logger
     */
    private val logger = Logger.get(this::class.java)

    /**
     * Current [ParserStatus]
     */
    private var status: ParserStatus by Delegates.observable(ParserStatus.INACTIVE) { _, _, new ->
        logger.info("Parser status set to $new")
    }

    /**
     * Parser configuration
     */
    private var config: Configuration by Delegates.observable(config) { _, _, new ->
        logger.info("Active parser configuration:\n$new")
    }

    init {
        logger.info("Active parser configuration:\n$config")
    }

    override fun configure(config: Configuration) {
        this.config = config
    }

    override fun parse(filepath: String): OSMDataSet {
        // pre-processing
        status = ParserStatus.PRE_PROCESSING
        val ifcFilepath: String = BIMFileOptimizer.optimizeIfcFile(
            filepath,
            config.optimizeInput_RBC,
            config.optimizeInput_RBL
        ).absolutePath

        // load into model and extract ifc environment vars
        status = ParserStatus.LOADING
        val model = Loader.loadIntoModel(ifcFilepath)
        val units = PropertiesExtractor.extractIfcUnits(model)

        // transform ifc to osm
        status = ParserStatus.PARSING
        val osmData = transformToOSM(model, units)

        // post-processing
        if (config.optimizeOutput_DS) {
            status = ParserStatus.POST_PROCESSING
            // TODO implement
        }

        return osmData
    }

    override fun status(): String {
        return status.toString()
    }

    /**
     * Transform each object in [model] into OSM object and put it into [OSMDataSet]
     * @param model ifc model
     * @param units ifc unit prefix
     * @return [OSMDataSet]
     */
    private fun transformToOSM(model: IfcModelInterface, units: IfcUnitPrefix): OSMDataSet {
        val schema = model.modelMetaData.ifcHeader.ifcSchemaVersion

        // resolve placements
        val placementResolver = PlacementResolver()
        if (schema == Schema.IFC4.headerName) {
            model.getAllWithSubTypes(Ifc4_IfcWall::class.java).forEach { wall ->
                placementResolver.resolvePlacement(wall.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc4_IfcSlab::class.java).forEach { slab ->
                placementResolver.resolvePlacement(slab.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc4_IfcColumn::class.java).forEach { column ->
                placementResolver.resolvePlacement(column.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc4_IfcDoor::class.java).forEach { door ->
                placementResolver.resolvePlacement(door.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc4_IfcStair::class.java).forEach { stair ->
                placementResolver.resolvePlacement(stair.objectPlacement)
            }
        }
        if (schema == Schema.IFC2X3TC1.headerName) {
            model.getAllWithSubTypes(Ifc2x3tc1_IfcWall::class.java).forEach { wall ->
                placementResolver.resolvePlacement(wall.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcSlab::class.java).forEach { slab ->
                placementResolver.resolvePlacement(slab.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcColumn::class.java).forEach { column ->
                placementResolver.resolvePlacement(column.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcDoor::class.java).forEach { door ->
                placementResolver.resolvePlacement(door.objectPlacement)
            }
            model.getAllWithSubTypes(Ifc2x3tc1_IfcStair::class.java).forEach { stair ->
                placementResolver.resolvePlacement(stair.objectPlacement)
            }
        }

        // TODO resolve representation

        return OSMDataSet()
    }

}