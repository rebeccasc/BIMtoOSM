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

import de.rebsc.bimtoosm.utils.math.Vector3D
import org.bimserver.models.ifc4.IfcProductRepresentation as Ifc4_IfcProductRepresentation
import org.bimserver.models.ifc4.IfcRepresentation as Ifc4_IfcRepresentation
import org.bimserver.models.ifc2x3tc1.IfcRepresentation as Ifc2x3tc1_IfcRepresentation
import org.bimserver.models.ifc2x3tc1.IfcProductRepresentation as Ifc2x3tc1_IfcProductRepresentation

class GeometryResolver {

    private var geometryCacheIfc4: MutableMap<Ifc4_IfcRepresentation, List<Vector3D>> = HashMap()
    private var geometryCacheIfc2x3tc1: MutableMap<Ifc2x3tc1_IfcRepresentation, List<Vector3D>> = HashMap()


    fun resolveWall(representation: Ifc4_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveWall(representation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveSlab(representation: Ifc4_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveSlab(representation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveColumn(representation: Ifc4_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveColumn(representation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveDoor(representation: Ifc4_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveDoor(representation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveStair(representation: Ifc4_IfcProductRepresentation) {
        // TODO implement
    }

    fun resolveStair(representation: Ifc2x3tc1_IfcProductRepresentation) {
        // TODO implement
    }

}