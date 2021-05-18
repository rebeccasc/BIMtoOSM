package de.rebsc.bimtoosm.geometry

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