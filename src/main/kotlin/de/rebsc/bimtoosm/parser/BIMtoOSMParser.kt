package de.rebsc.bimtoosm.parser

import de.rebsc.bimtoosm.api.BIMtoOSM
import de.rebsc.bimtoosm.data.OSMDataSet

class BIMtoOSMParser(config: Configuration) : BIMtoOSM {

    // parser configuration
    val config: Configuration = config

    init {
        println("init parser")
    }

    override fun configure(config: Configuration) {
        TODO("Not yet implemented")
    }

    override fun parse(filepath: String): OSMDataSet {
        TODO("Not yet implemented")
    }
}