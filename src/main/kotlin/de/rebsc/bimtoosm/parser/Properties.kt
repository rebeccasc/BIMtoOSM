package de.rebsc.bimtoosm.parser

import nl.tue.buildingsmart.express.population.EntityInstance
import nl.tue.buildingsmart.express.population.ModelPopulation
import java.util.*

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

enum class ParserStatus {

    INACTIVE, PRE_PROCESSING, LOADING, PARSING, POST_PROCESSING
}

enum class IfcSchema(val value: String) {
    IFC2X3_TC1("IFC2X3_TC1"),
    IFC4("IFC4"),
    FLAG_IFC2X3_TC1("FILE_SCHEMA(('IFC2X3_TC1'))"),
    FLAG_IFC2X3("FILE_SCHEMA(('IFC2X3'))"),
    FLAG_IFC4("FILE_SCHEMA(('IFC4'))")
}

enum class IfcSIUnitName {
    AMPERE,
    BECQUEREL,
    CANDELA,
    COULOMB,
    CUBIC_METRE,
    DEGREE_CELSIUS,
    FARAD,
    GRAM,
    GRAY,
    HENRY,
    HERTZ,
    JOULE,
    KELVIN,
    LUMEN,
    LUX,
    METRE,
    MOLE,
    NEWTON,
    OHM,
    PASCAL,
    RADIAN,
    SECOND,
    SIEMENS,
    SIEVERT,
    SQUARE_METRE,
    STERADIAN,
    TESLA,
    VOLT,
    WATT,
    WEBER
}

/**
 * As described in standard
 * @see https://standards.buildingsmart.org/IFC/DEV/IFC4_2/FINAL/HTML/schema/ifcmeasureresource/lexical/ifcsiprefix.htm
 */
enum class IfcSIPrefix {

    NONE,   // default
    EXA,
    PETA,
    TERA,
    GIGA,
    MEGA,
    KILO,
    HECTO,
    DECA,
    DECI,
    CENTI,
    MILLI,
    MICRO,
    NANO,
    PICO,
    FEMTO,
    ATTO
}


/**
 * Class proved methods to extract ifc properties from file
 */
class PropertiesExtractor {

    companion object {

        /**
         * Extract ifc units/ unit prefix used in [ifcModel]
         * @param ifcModel ifc model population
         * @return [IfcUnitPrefix] object holding prefix for different units
         */
        fun extractIfcUnits(ifcModel: ModelPopulation): IfcUnitPrefix {
            val unitEntityInstances = ifcModel.getInstancesOfType("IFCSIUNIT")
            var length = IfcSIPrefix.NONE
            var area = IfcSIPrefix.NONE
            var volume = IfcSIPrefix.NONE
            var planeAngle = IfcSIPrefix.NONE

            unitEntityInstances.toList().forEach {
                val unitType = it.getAttributeValueBN("UnitType")
                val unitPrefix = it.getAttributeValueBN("Prefix")
                when (unitType) {
                    ".LENGTHUNIT." -> {
                        IfcSIPrefix.values().forEach { prefix ->
                            if (unitPrefix.toString().contains(prefix.name)) length = prefix
                        }
                    }
                    ".AREAUNIT." -> {
                        IfcSIPrefix.values().forEach { prefix ->
                            if (unitPrefix.toString().contains(prefix.name)) area = prefix
                        }
                    }
                    ".VOLUMEUNIT." -> {
                        IfcSIPrefix.values().forEach { prefix ->
                            if (unitPrefix.toString().contains(prefix.name)) volume = prefix
                        }
                    }
                    ".PLANEANGLEUNIT." -> {
                        IfcSIPrefix.values().forEach { prefix ->
                            if (unitPrefix.toString().contains(prefix.name)) planeAngle = prefix
                        }
                    }
                }
            }

            return IfcUnitPrefix(length, area, volume, planeAngle)
        }
    }

}

/**
 * Class holding Ifc units data
 */
data class IfcUnitPrefix(
    val lengthUnitPrefix: IfcSIPrefix,
    val areaUnitPrefix: IfcSIPrefix,
    val volumeUnitPrefix: IfcSIPrefix,
    val planeAngleUnitPrefix: IfcSIPrefix
)
