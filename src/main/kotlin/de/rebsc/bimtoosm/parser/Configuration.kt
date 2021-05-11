package de.rebsc.bimtoosm.parser

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import de.rebsc.bimtoosm.geometry.GeometrySolution

/**
 * Configures the [BIMtoOSMParser]
 */
class Configuration() {
    private var solution: GeometrySolution = GeometrySolution.BOUNDING_BOX
    private var optimizeInput: Boolean = false
    private var optimizeOutput: Boolean = false
    private var optimizeOutputMergeDist: Double = 0.1

    private val exceptionMsg = "Invalid configuration value for param -optimizeOutputMergeDist-"

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

}