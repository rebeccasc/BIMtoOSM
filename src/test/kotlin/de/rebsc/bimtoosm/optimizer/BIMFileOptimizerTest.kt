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
            val filepath = "$dir/src/test/resources/ifc4/invalidFilePath.ifc".replace("/", File.separator)
            BIMFileOptimizer.optimizeIfcFile(filepath, optimizeInput_RBC = false, optimizeInput_RBL = false)
        }

        //------------ test valid file ------------ //
        // test valid filepath
        Assertions.assertDoesNotThrow {
            val filepath = "$dir/src/test/resources/ifc4/kfz_house_IFC4.ifc".replace("/", File.separator)
            BIMFileOptimizer.optimizeIfcFile(filepath, optimizeInput_RBC = false, optimizeInput_RBL = false)
        }

        // test remove block comments
        val fileReferenceBC = File(
            "$dir/src/test/resources/ifc2x3tc1/house_1_IFC2X3TC1.ifc".replace("/", File.separator)
        )
        val fileOptimizedBC =
            BIMFileOptimizer.optimizeIfcFile(
                "$dir/src/test/resources/ifc2x3tc1/house_1_IFC2X3TC1_BC.ifc".replace("/", File.separator),
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
        val fileReferenceBLBC = File(
            "$dir/src/test/resources/ifc4/kfz_house_IFC4.ifc".replace("/", File.separator)
        )
        val fileOptimizedBLBC =
            BIMFileOptimizer.optimizeIfcFile(
                "$dir/src/test/resources/ifc4/kfz_house_IFC4_BC_BL.ifc".replace("/", File.separator),
                optimizeInput_RBC = true,
                optimizeInput_RBL = true
            )
        val referenceBL = fileReferenceBLBC.useLines { it.toList() }
        val optimizedBL = fileOptimizedBLBC.useLines { it.toList() }
        // check if entries equal
        Assertions.assertTrue(referenceBL.zip(optimizedBL).all { (x, y) -> x == y })

    }

}