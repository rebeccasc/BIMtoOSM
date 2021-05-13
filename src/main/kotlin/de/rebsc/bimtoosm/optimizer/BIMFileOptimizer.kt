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
         * Removes block comments from ifc file
         * @param filepath to ifc file
         * @return temporary file without block comments
         * @throws BIMtoOSMException if error occurred while reading the file
         */
        fun removeBlockComments(filepath: String): File {
            val tempFile = File.createTempFile("BIMtoOSM_temp", ".ifc")
            tempFile.deleteOnExit()

            val optimizedContent = StringBuilder()
            try {
                File(filepath).forEachLine {
                    val repLine = it.replace(Regex("/\\*.*?\\*/"), "")
                        .replace(Regex("\\*/.*?\\*/"), "")
                    optimizedContent.append(repLine).append("\n")
                }
            } catch (e: FileNotFoundException) {
                throw BIMtoOSMException("File not found: $filepath")
            }

            tempFile.writeText(optimizedContent.toString())
            return tempFile
        }
    }

}