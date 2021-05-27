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


class Matrix3DTest {

    // Test data setup
    private val matrix = Matrix3D(
        0.0, 0.0, 0.0,
        0.0, 0.0, 1.0,
        1.0, 0.0, 0.0
    )
    private val matrix2 = Matrix3D(
        1.0, 2.0, 0.0,
        2.0, 4.0, 1.0,
        2.0, 1.0, 0.0
    )
    private val vector = Vector3D(10.0, 0.0, 3.0)

    @Test
    fun transformTest() {
        matrix.transform(vector)
        assertEquals(0.0, vector.x)
        assertEquals(3.0, vector.y)
        assertEquals(10.0, vector.z)
    }

    @Test
    fun invertTest() {
        matrix2.invert()
        assertEquals(-0.3333333333333333, matrix2.m00)
        assertEquals(0.0, matrix2.m01)
        assertEquals(0.6666666666666666, matrix2.m02)

        assertEquals(0.6666666666666666, matrix2.m10)
        assertEquals(0.0, matrix2.m11)
        assertEquals(-0.3333333333333333, matrix2.m12)

        assertEquals(-2.0, matrix2.m20)
        assertEquals(1.0, matrix2.m21)
        assertEquals(0.0, matrix2.m22)
    }

    // TODO test other methods

}