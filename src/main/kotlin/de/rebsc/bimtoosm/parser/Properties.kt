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

enum class ParserStatus {
    INACTIVE, PRE_PROCESSING, LOADING, PARSING, POST_PROCESSING
}

enum class IFCSchema(val value: String) {
    IFC2X3_TC1("IFC2X3_TC1"),
    IFC4("IFC4"),
    FLAG_IFC2X3_TC1("FILE_SCHEMA(('IFC2X3_TC1'))"),
    FLAG_IFC2X3("FILE_SCHEMA(('IFC2X3'))"),
    FLAG_IFC4("FILE_SCHEMA(('IFC4'))")
}

enum class LengthUnit {
    METER, CENTI, MILLI
}

enum class AreaUnit {
    SQUARE_METRE
}

enum class VolumeUnit {
    CUBIC_METRE
}

enum class PlaneAngleUnit {
    RAD, DEG
}