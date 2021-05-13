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
import de.rebsc.bimtoosm.logger.Logger
import de.rebsc.bimtoosm.optimizer.BIMFileOptimizer
import java.io.File
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
        var ifcFilepath = filepath

        // pre-processing
        if (config.optimizeInput_RBC) {
            status = ParserStatus.PRE_PROCESSING
            ifcFilepath = BIMFileOptimizer.removeBlockComments(filepath).absolutePath
        }

        // load into model and extract ifc environment vars
        status = ParserStatus.LOADING
        val ifcModel = Loader.loadIntoModel(ifcFilepath)
        // TODO implement extraction

        // transform ifc to osm
        status = ParserStatus.PARSING
        // TODO implement

        // post-processing
        if (config.optimizeOutput_DS) {
            status = ParserStatus.POST_PROCESSING
            // TODO implement
        }

        return OSMDataSet()
    }

    override fun status(): String {
        return status.toString()
    }

}