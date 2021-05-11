package de.rebsc.bimtoosm.parser

import de.rebsc.bimtoosm.api.BIMtoOSM
import de.rebsc.bimtoosm.data.OSMDataSet

class BIMtoOSMParser(private var config: Configuration) : BIMtoOSM {

    override fun configure(config: Configuration) {
        this.config = config
    }

    override fun parse(filepath: String): OSMDataSet {
        TODO("Not yet implemented")
    }
}