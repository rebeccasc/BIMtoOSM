package de.rebsc.bimtoosm.geometry

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

/**
 * Geometry engine solutions
 */
enum class GeometrySolution {

    BODY, BOUNDING_BOX
}

/**
 * Currently supported object types
 */
enum class SupportedObjectType {

    IFC_WALL, IFC_SLAB, IFC_SPACE, IFC_COLUMN, IFC_DOOR, IFC_WINDOW, IFC_STAIR
}