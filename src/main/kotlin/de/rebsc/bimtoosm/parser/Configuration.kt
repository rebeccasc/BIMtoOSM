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

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import de.rebsc.bimtoosm.geometry.GeometrySolution

/**
 * Configures the [BIMtoOSMParser]
 */
class Configuration() {

    /**
     * Solution engine
     */
    var solution: GeometrySolution = GeometrySolution.BOUNDING_BOX

    /**
     * Remove block comments from ifc input file
     */
    var optimizeInput_RBC: Boolean = false

    /**
     * Reduce data size of OSM output
     */
    var optimizeOutput_DS: Boolean = false

    /**
     * Set merge distance for OSM output optimization,
     * only used if [optimizeOutput_DS] set true
     */
    var optimizeOutput_DSMD: Double = 0.1

    /**
     * Exception message thrown at invalid [optimizeOutput_DSMD] values
     */
    private val exceptionMsg = "Invalid configuration value for param [optimizeOutput_DSMD]"

    constructor(
        solution: GeometrySolution,
        optimizeInput_RBC: Boolean,
        optimizeOutput_DS: Boolean,
        optimizeOutput_DSMD: Double
    ) : this() {
        this.solution = solution
        this.optimizeInput_RBC = optimizeInput_RBC
        this.optimizeOutput_DS = optimizeOutput_DS
        if (optimizeOutput_DSMD.isNaN()) throw BIMtoOSMException(exceptionMsg)
        if (optimizeOutput_DSMD.isInfinite()) throw BIMtoOSMException(exceptionMsg)
        if (optimizeOutput_DSMD < 0.0) throw BIMtoOSMException(exceptionMsg)
        this.optimizeOutput_DSMD = optimizeOutput_DSMD
    }

    override fun toString(): String {
        return "\t SolutionEngine set to $solution\n" +
                "\t Optimize Input set $optimizeInput_RBC\n" +
                "\t Optimize Output set $optimizeOutput_DS; MergeDistance set to $optimizeOutput_DSMD"
    }

}