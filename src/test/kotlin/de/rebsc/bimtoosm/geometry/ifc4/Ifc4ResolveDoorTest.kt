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

internal class Ifc4ResolveDoorTest {
    // Test setup

    // URLs

    // Parser
    private val placementResolver = Ifc4PlacementResolver()
    private val geometryResolverBody = GeometryResolver(GeometrySolution.BODY)
    private val geometryResolverBB = GeometryResolver(GeometrySolution.BOUNDING_BOX)


    @Test
    @Description("")
    fun resolveDoorBody() {
        // TODO implement
    }

    @Test
    @Description("")
    fun resolveDoorBB() {
        // TODO implement
    }
}