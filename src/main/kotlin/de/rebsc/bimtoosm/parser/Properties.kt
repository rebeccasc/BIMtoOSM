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

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import org.bimserver.emf.IfcModelInterface
import org.bimserver.emf.Schema
import org.eclipse.emf.common.util.Enumerator
import org.bimserver.models.ifc2x3tc1.IfcSIPrefix as Ifc2x3tc1_IfcSIPrefix
import org.bimserver.models.ifc2x3tc1.IfcSIUnit as Ifc2x3tc1_IfcSIUnit
import org.bimserver.models.ifc2x3tc1.IfcSIUnitName as Ifc2x3tc1_IfcSIUnitName
import org.bimserver.models.ifc4.IfcSIPrefix as Ifc4_IfcSIPrefix
import org.bimserver.models.ifc4.IfcSIUnit as Ifc4_IfcSIUnit
import org.bimserver.models.ifc4.IfcSIUnitName as Ifc4_IfcSIUnitName

enum class ParserStatus {

    INACTIVE, PRE_PROCESSING, LOADING, PARSING, POST_PROCESSING
}

/**
 * Class provides methods to extract ifc properties from file
 */
class PropertiesExtractor {

    companion object {

        /**
         * Extract ifc units/ unit prefix used in [model]
         * @param model ifc model population
         * @return [IfcUnitPrefix] object holding prefix for different units
         */
        fun extractIfcUnits(model: IfcModelInterface): IfcUnitPrefix {
            val schema = model.modelMetaData.ifcHeader.ifcSchemaVersion

            if (schema == Schema.IFC4.headerName) {
                val unitEntityInstances = model.getAllWithSubTypes(Ifc4_IfcSIUnit::class.java)
                var length = Ifc4_IfcSIPrefix.NULL
                var area = Ifc4_IfcSIPrefix.NULL
                var volume = Ifc4_IfcSIPrefix.NULL
                var planeAngle = Ifc4_IfcSIPrefix.NULL

                unitEntityInstances.forEach {
                    when (it.name) {
                        Ifc4_IfcSIUnitName.METRE -> length = it.prefix
                        Ifc4_IfcSIUnitName.SQUARE_METRE -> area = it.prefix
                        Ifc4_IfcSIUnitName.CUBIC_METRE -> volume = it.prefix
                        Ifc4_IfcSIUnitName.RADIAN -> planeAngle = it.prefix
                        else -> return@forEach
                    }
                }
                return IfcUnitPrefix(length, area, volume, planeAngle)
            }

            if (schema == Schema.IFC2X3TC1.headerName) {
                val unitEntityInstances = model.getAllWithSubTypes(Ifc2x3tc1_IfcSIUnit::class.java)
                var length = Ifc2x3tc1_IfcSIPrefix.NULL
                var area = Ifc2x3tc1_IfcSIPrefix.NULL
                var volume = Ifc2x3tc1_IfcSIPrefix.NULL
                var planeAngle = Ifc2x3tc1_IfcSIPrefix.NULL

                unitEntityInstances.forEach {
                    when (it.name) {
                        Ifc2x3tc1_IfcSIUnitName.METRE -> length = it.prefix
                        Ifc2x3tc1_IfcSIUnitName.SQUARE_METRE -> area = it.prefix
                        Ifc2x3tc1_IfcSIUnitName.CUBIC_METRE -> volume = it.prefix
                        Ifc2x3tc1_IfcSIUnitName.RADIAN -> planeAngle = it.prefix
                        else -> return@forEach
                    }
                }
                return IfcUnitPrefix(length, area, volume, planeAngle)
            }

            throw BIMtoOSMException("Could not extract ifc unit data")
        }
    }

}

/**
 * Class holding Ifc units data
 */
data class IfcUnitPrefix(
    val lengthUnitPrefix: Enumerator,
    val areaUnitPrefix: Enumerator,
    val volumeUnitPrefix: Enumerator,
    val planeAngleUnitPrefix: Enumerator
)
