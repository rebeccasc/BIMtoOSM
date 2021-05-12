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

class BIMtoOSMParser(private var config: Configuration) : BIMtoOSM {

    override fun configure(config: Configuration) {
        this.config = config
    }

    override fun parse(filepath: String): OSMDataSet {
        // TODO optimize file if configured
        // TODO extract file schema
        val ifcSchemaFilepath = "${System.getProperty("user.dir")}/src/main/resources/IFC4.exp"
        val ifcModel = Loader.loadIntoModel(filepath, ifcSchemaFilepath)
        // TODO extract ifc environment vars
        // TODO ...

        return OSMDataSet()
    }

}