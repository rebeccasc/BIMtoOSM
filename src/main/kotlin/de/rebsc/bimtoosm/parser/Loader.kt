package de.rebsc.bimtoosm.parser

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import nl.tue.buildingsmart.express.population.ModelPopulation
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.NoSuchFileException
import java.nio.file.Paths

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

class Loader {

    companion object {
        /**
         * Loads ifc file into model
         * @param filepath to ifc file
         * @param ifcSchemaFilePath to ifc schema (IFC4, ...)
         * @return ifc model population including ifc objects or
         * @throws BIMtoOSMException
         */
        fun loadIntoModel(filepath: String, ifcSchemaFilePath: String): ModelPopulation {
            try {
                val file = File(filepath)
                val inputFs = file.inputStream()
                val ifcModel = ModelPopulation(inputFs)
                ifcModel.schemaFile = Paths.get(ifcSchemaFilePath)
                ifcModel.load()
                inputFs.close()
                return ifcModel
            } catch (e: Exception) {
                when (e) {
                    is FileNotFoundException, is NoSuchFileException -> throw BIMtoOSMException("File not found: $filepath")
                    is NullPointerException -> throw BIMtoOSMException("Invalid IFC Schema file: $filepath")
                    else -> throw e
                }
            }
        }

    }

}