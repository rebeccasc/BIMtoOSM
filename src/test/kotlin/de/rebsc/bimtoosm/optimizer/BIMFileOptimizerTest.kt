package de.rebsc.bimtoosm.optimizer

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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File


internal class BIMFileOptimizerTest {

    @Test
    fun removeBlockCommentsTest() {
        val dir = System.getProperty("user.dir")

        // test invalid filepath
        Assertions.assertThrows(BIMtoOSMException::class.java) {
            val filepath = "$dir/src/test/resources/test1_IFC4_.ifc"
            BIMFileOptimizer.removeBlockComments(filepath)
        }

        // test valid filepath
        Assertions.assertDoesNotThrow {
            val filepath = "$dir/src/test/resources/test1_IFC4.ifc"
            BIMFileOptimizer.removeBlockComments(filepath)
        }

        // test remove block comments
        val fileOriginal = File("$dir/src/test/resources/test2_IFC2X3_TC1.ifc")
        val fileWithoutBC = BIMFileOptimizer.removeBlockComments("$dir/src/test/resources/test2_IFC2X3_TC1_BC.ifc")
        val original = fileOriginal.useLines { it.toList() }
        val withoutBC = fileWithoutBC.useLines { it.toList() }
        // check if same size
        Assertions.assertTrue(original.size == withoutBC.size)
        // check if entries equal
        Assertions.assertTrue(original.zip(withoutBC).all { (x, y) -> x == y })
    }

}