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
import de.rebsc.bimtoosm.exception.BIMtoOSMException
import nl.tue.buildingsmart.express.population.ModelPopulation
import java.io.File
import java.nio.file.Paths

class BIMtoOSMParser(private var config: Configuration) : BIMtoOSM {

    private lateinit var file: File
    private lateinit var ifcModel: ModelPopulation

    override fun configure(config: Configuration) {
        this.config = config
    }

    override fun parse(filepath: String): OSMDataSet {
        loadFile(filepath)
        // TODO implement

        return OSMDataSet()
    }

    /**
     * Loads ifc file into model
     * @throws BIMtoOSMException
     */
    private fun loadFile(filepath: String) {
        try {
            file = File(filepath)
            val inputFs = file.inputStream()
            ifcModel = ModelPopulation(inputFs)

            // TODO fix - implementation for testing only
            val ifcSchemaFilePath = System.getProperty("user.dir") + "/src/main/resources/IFC4.exp"
            ifcModel.schemaFile = Paths.get(ifcSchemaFilePath)
            ifcModel.load()
            inputFs.close()

        } catch (e: NullPointerException) {
            throw BIMtoOSMException("Could not load $filepath")
        }
    }
}