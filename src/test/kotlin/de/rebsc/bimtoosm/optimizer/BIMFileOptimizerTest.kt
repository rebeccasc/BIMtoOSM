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
    fun optimizeIfcFile() {
        val dir = System.getProperty("user.dir")

        //------------ test invalid file ------------ //
        // test invalid filepath
        Assertions.assertThrows(BIMtoOSMException::class.java) {
            val filepath = "$dir/src/test/resources/test1_IFC4_.ifc"
            BIMFileOptimizer.optimizeIfcFile(filepath, optimizeInput_RBC = false, optimizeInput_RBL = false)
        }

        //------------ test valid file ------------ //
        // test valid filepath
        Assertions.assertDoesNotThrow {
            val filepath = "$dir/src/test/resources/test1_IFC4.ifc"
            BIMFileOptimizer.optimizeIfcFile(filepath, optimizeInput_RBC = false, optimizeInput_RBL = false)
        }

        // test remove block comments
        val fileReferenceBC = File("$dir/src/test/resources/test2_IFC2X3_TC1.ifc")
        val fileOptimizedBC =
            BIMFileOptimizer.optimizeIfcFile(
                "$dir/src/test/resources/test2_IFC2X3_TC1_BC.ifc",
                optimizeInput_RBC = true,
                optimizeInput_RBL = false
            )
        val referenceBC = fileReferenceBC.useLines { it.toList() }
        val optimizedBC = fileOptimizedBC.useLines { it.toList() }
        // check if same size
        Assertions.assertTrue(referenceBC.size == optimizedBC.size)
        // check if entries equal
        Assertions.assertTrue(referenceBC.zip(optimizedBC).all { (x, y) -> x == y })


        // test remove block comments and blank lines
        val fileReferenceBL = File("$dir/src/test/resources/test1_IFC4_WBL.ifc")
        val fileOptimizedBL =
            BIMFileOptimizer.optimizeIfcFile(
                "$dir/src/test/resources/test1_IFC4.ifc",
                optimizeInput_RBC = true,
                optimizeInput_RBL = true
            )
        val referenceBL = fileReferenceBL.useLines { it.toList() }
        val optimizedBL = fileOptimizedBL.useLines { it.toList() }
        // check if entries equal
        Assertions.assertTrue(referenceBL.zip(optimizedBL).all { (x, y) -> x == y })

    }

}