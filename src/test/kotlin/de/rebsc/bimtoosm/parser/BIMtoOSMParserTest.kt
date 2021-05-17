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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File


internal class BIMtoOSMParserTest {

    @Test
    fun initTest() {
        // test negative optimizer merge distance
        Assertions.assertThrows(BIMtoOSMException::class.java) {
            val invalidConfig = Configuration(
                solution = GeometrySolution.BODY,
                optimizeInput_RBC = true,
                optimizeInput_RBL = true,
                optimizeOutput_DS = false,
                optimizeOutput_DSMD = -0.1
            )
            BIMtoOSMParser(invalidConfig)
        }

        // test infinite optimizer merge distance
        Assertions.assertThrows(BIMtoOSMException::class.java) {
            val invalidConfig = Configuration(
                solution = GeometrySolution.BODY,
                optimizeInput_RBC = false,
                optimizeInput_RBL = false,
                optimizeOutput_DS = false,
                optimizeOutput_DSMD = Double.POSITIVE_INFINITY
            )
            BIMtoOSMParser(invalidConfig)
        }

        // test positive optimizer merge distance
        Assertions.assertDoesNotThrow {
            val validConfig = Configuration(
                solution = GeometrySolution.BOUNDING_BOX,
                optimizeInput_RBC = true,
                optimizeInput_RBL = true,
                optimizeOutput_DS = true,
                optimizeOutput_DSMD = 0.05
            )
            BIMtoOSMParser(validConfig)
        }
    }

    @Test
    @Disabled
    fun parseTest() {
        // TODO implement
        val validConfig = Configuration(
            solution = GeometrySolution.BOUNDING_BOX,
            optimizeInput_RBC = true,
            optimizeInput_RBL = true,
            optimizeOutput_DS = true,
            optimizeOutput_DSMD = 0.05
        )
        val dir = System.getProperty("user.dir")
        BIMtoOSMParser(validConfig).parse("$dir\\src\\test\\resources\\test2_IFC2X3_TC1.ifc")
    }

}