package de.rebsc.bimtoosm.geometry.ifc4

import de.rebsc.bimtoosm.geometry.GeometrySolution
import jdk.jfr.Description
import org.junit.jupiter.api.Test

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

internal class Ifc4ResolveColumnTest {
    // Test setup

    // URLs

    // Parser
    private val placementResolver = Ifc4PlacementResolver()
    private val geometryResolverBody = Ifc4GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB = Ifc4GeometryResolver(GeometrySolution.BOUNDING_BOX)


    @Test
    @Description("")
    fun resolveColumnBody() {
        // TODO implement
    }

    @Test
    @Description("")
    fun resolveColumnBB() {
        // TODO implement
    }
}