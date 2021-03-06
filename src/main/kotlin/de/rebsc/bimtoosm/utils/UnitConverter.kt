package de.rebsc.bimtoosm.utils

import de.rebsc.bimtoosm.utils.math.Point3D
import org.eclipse.emf.common.util.Enumerator

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

class UnitConverter {

    companion object{

        fun toMeter(point: Point3D, unit: Enumerator): Point3D{
            when(unit.name){
                "NULL" -> return point
                "MILLI" -> {
                    return Point3D(point.x/1000.0, point.y/1000.0, point.z/1000.0)
                }
                // TODO implement more unit converts
            }
            return point
        }

    }
}