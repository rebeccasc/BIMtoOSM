package de.rebsc.bimtoosm.api

import de.rebsc.bimtoosm.data.OSMDataSet
import de.rebsc.bimtoosm.parser.Configuration

interface BIMtoOSM {

    /**
     * Configure the parser
     */
    fun configure(config: Configuration)

    /**
     * Parse BIM file placed at filepath to OSM data
     */
    fun parse(filepath: String): OSMDataSet
}