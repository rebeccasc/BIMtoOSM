package de.rebsc.bimtoosm.optimizer

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
import java.io.File
import java.io.FileNotFoundException


class BIMFileOptimizer {

    companion object {

        /**
         * Optimizes ifc file following the configuration
         * @param filepath to ifc file
         * @param optimizeInput_RBC true to remove block comments from file
         * @param optimizeInput_RBL true to remove blank lines from file
         * @return temporary file without block comments
         * @throws BIMtoOSMException if error occurred while reading the file
         */
        fun optimizeIfcFile(filepath: String, optimizeInput_RBC: Boolean, optimizeInput_RBL: Boolean): File {
            if (!optimizeInput_RBC && !optimizeInput_RBL) {
                val original = File(filepath)
                if (!original.exists()) throw BIMtoOSMException("File not found: $filepath")
                return original
            }

            val tempFile = File.createTempFile("BIMtoOSM_temp", ".ifc")
            tempFile.deleteOnExit()

            val optimizedContent = StringBuilder()
            try {
                File(filepath).forEachLine {
                    var optimizedLine = it

                    // remove block comments
                    if (optimizeInput_RBC) {
                        optimizedLine = it.replace(Regex("/\\*.*?\\*/"), "")
                            .replace(Regex("\\*/.*?\\*/"), "")
                    }

                    // remove blank lines
                    if (optimizeInput_RBL) {
                        if (optimizedLine.isEmpty()) return@forEachLine
                    }

                    optimizedContent.append(optimizedLine).append("\n")
                }
            } catch (e: FileNotFoundException) {
                throw BIMtoOSMException("File not found: $filepath")
            }

            if (optimizedContent.toString().isEmpty()) {
                throw BIMtoOSMException("Optimized file empty")
            }

            tempFile.writeText(optimizedContent.toString())
            return tempFile
        }
    }

}