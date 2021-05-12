package de.rebsc.bimtoosm.parser

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

import de.rebsc.bimtoosm.exception.BIMtoOSMException
import de.rebsc.bimtoosm.geometry.GeometrySolution
import org.junit.Assert.assertThrows
import org.junit.Test

internal class BIMtoOSMParserTest {

    @Test
    fun initTest() {
        assertThrows(BIMtoOSMException::class.java) {
            val invalidConfig = Configuration(
                GeometrySolution.BODY,
                true,
                false,
                -0.1
            )
            BIMtoOSMParser(invalidConfig)
        }

        assertThrows(BIMtoOSMException::class.java) {
            val invalidConfig = Configuration(
                GeometrySolution.BODY,
                false,
                false,
                Double.POSITIVE_INFINITY
            )
            BIMtoOSMParser(invalidConfig)
        }

        val validConfig = Configuration(
            GeometrySolution.BOUNDING_BOX,
            true,
            true,
            0.05
        )
        BIMtoOSMParser(validConfig)
    }

    @Test
    fun parseTest() {
        // TODO fix - implementation for testing only
        val dir = System.getProperty("user.dir")
        val filepath = "$dir/src/test/resources/test1_IFC4.ifc"
        BIMtoOSMParser(Configuration()).parse(filepath)
    }

}