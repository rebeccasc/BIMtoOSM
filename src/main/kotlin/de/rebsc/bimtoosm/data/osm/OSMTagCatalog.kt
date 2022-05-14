package de.rebsc.bimtoosm.data.osm

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

import de.rebsc.bimtoosm.geometry.SupportedObjectType

class OSMTagCatalog {

    companion object {

        /**
         * Osm tags for [SupportedObjectType]
         */
        fun osmTagsFor(type: SupportedObjectType): ArrayList<OSMTag> {
            val tags = ArrayList<OSMTag>()
            if (type == SupportedObjectType.IFC_WALL) {
                tags.add(OSMTag("indoor", "wall"))
                tags.add(OSMTag("material", "concrete"))
            }
            if (type == SupportedObjectType.IFC_SLAB) {
                tags.add(OSMTag("indoor", "area"))
            }
            if (type == SupportedObjectType.IFC_COLUMN) {
                tags.add(OSMTag("indoor", "area"))
            }
            if (type == SupportedObjectType.IFC_WINDOW) {
                tags.add(OSMTag("indoor", "wall"))
                tags.add(OSMTag("material", "glass"))
            }
            if (type == SupportedObjectType.IFC_DOOR) {
                tags.add(OSMTag("door", "yes"))
            }
            if (type == SupportedObjectType.IFC_STAIR) {
                tags.add(OSMTag("highway", "steps"))
            }
            return tags
        }

    }


}