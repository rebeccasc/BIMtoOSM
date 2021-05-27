package de.rebsc.bimtoosm.utils.math

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


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Vector3DTest {

    // Test data setup
    private val vector = Vector3D(3.0, 1.0, 2.0)
    private val vector1 = Vector3D(3.0, 1.0, 2.0)

    @Test
    fun normalizeTest() {
        vector.normalize()
        assertEquals(0.8017837257372732, vector.x)
        assertEquals(0.2672612419124244, vector.y)
        assertEquals(0.5345224838248488, vector.z)
    }

    // TODO test other methods
}