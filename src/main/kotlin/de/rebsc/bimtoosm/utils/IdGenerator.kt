package de.rebsc.bimtoosm.utils

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

import java.util.*

class IdGenerator {

    companion object {

        /**
         * Generates unique id
         * @param allowNegative true if negative ids are allowed, else false
         * @return id
         */
        fun createUUID(allowNegative: Boolean): Long {
            val id = (UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE).toInt().toLong()
            if (!allowNegative) {
                if (id >= 0) return id
                return id * -1
            } else {
                if (id < 0) return id
                return id * -1
            }
        }

    }
}