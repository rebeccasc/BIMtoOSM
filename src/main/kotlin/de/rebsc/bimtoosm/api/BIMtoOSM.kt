package de.rebsc.bimtoosm.api

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

import de.rebsc.bimtoosm.data.osm.OSMDataSet
import de.rebsc.bimtoosm.parser.Configuration

interface BIMtoOSM {

    /**
     * Configure the parser
     * @param config Parser configuration
     */
    fun configure(config: Configuration)

    /**
     * Parse BIM file placed at [filepath] to OSM data
     * @param filepath BIM file
     */
    fun parse(filepath: String): OSMDataSet

    /**
     * Gives information about current parser status
     */
    fun status(): String

    /**
     * Export data to .osm file located at [filepath]
     * @param filepath to export location
     * @param data [OSMDataSet] to export
     */
    fun export(filepath: String, data: OSMDataSet)

}