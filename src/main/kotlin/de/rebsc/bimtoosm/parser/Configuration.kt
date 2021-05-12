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
    var solution: GeometrySolution = GeometrySolution.BOUNDING_BOX
    var optimizeInput: Boolean = false
    var optimizeOutput: Boolean = false
    var optimizeOutputMergeDist: Double = 0.1

    private val exceptionMsg = "Invalid configuration value for param <optimizeOutputMergeDist>"

    constructor(
        solution: GeometrySolution,
        optimizeInput: Boolean,
        optimizeOutput: Boolean,
        optimizeOutputMergeDist: Double
    ) : this() {
        this.solution = solution
        this.optimizeInput = optimizeInput
        this.optimizeOutput = optimizeOutput
        if (optimizeOutputMergeDist.isNaN()) throw BIMtoOSMException(exceptionMsg)
        if (optimizeOutputMergeDist.isInfinite()) throw BIMtoOSMException(exceptionMsg)
        if (optimizeOutputMergeDist < 0.0) throw BIMtoOSMException(exceptionMsg)
        this.optimizeOutputMergeDist = optimizeOutputMergeDist
    }

    override fun toString(): String {
        return "\t SolutionEngine set to $solution\n" +
                "\t Optimize Input set $optimizeInput\n" +
                "\t Optimize Output set $optimizeOutput; MergeDistance set to $optimizeOutputMergeDist"
    }

}