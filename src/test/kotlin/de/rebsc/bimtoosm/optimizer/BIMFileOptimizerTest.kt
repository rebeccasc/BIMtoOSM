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
import java.io.IOException
import java.net.URL


internal class BIMFileOptimizerTest {

    // Test setup

    // URLs
    private val url_wall_with_window_IFC2X3 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/wall/ifcwallstandardcase/wall_single_with_window_IFC2X3.ifc")
    private val url_house_1_IFC2X3 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/building/house_1_IFC2X3.ifc")
    private val url_house_1_IFC2X3_BC =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc2X3/building/house_1_IFC2X3_BC.ifc")
    private val url_kfz_house_IFC4 =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc4/building/kfz_house_IFC4.ifc")
    private val url_kfz_house_IFC4_BC_BL =
        URL("https://raw.githubusercontent.com/rebeccasc/IfcTestFiles/master/ifc4/building/kfz_house_IFC4_BC_BL.ifc")

    @Test
    fun optimizeIfcFile() {
        val dir = System.getProperty("user.dir")

        //------------ test invalid file ------------ //
        // test invalid filepath
        Assertions.assertThrows(BIMtoOSMException::class.java) {
            val invalidFilePath = downloadFile(url_wall_with_window_IFC2X3, "tmpFileTest").name
            BIMFileOptimizer.optimizeIfcFile(
                File(invalidFilePath),
                optimizeInput_RBC = false,
                optimizeInput_RBL = false
            )
        }

        //------------ test valid file ------------ //
        // test valid filepath
        Assertions.assertDoesNotThrow {
            val filepath = downloadFile(url_kfz_house_IFC4, "tmpFileTest").path
            BIMFileOptimizer.optimizeIfcFile(File(filepath), optimizeInput_RBC = false, optimizeInput_RBL = false)
        }

        // test remove block comments
        val fileReferenceBC = downloadFile(url_house_1_IFC2X3, "url_house_1_IFC2X3Test")
        val fileOptimizedBC =
            BIMFileOptimizer.optimizeIfcFile(
                downloadFile(url_house_1_IFC2X3_BC, "url_house_1_IFC2X3_BCTest"),
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
        val fileReferenceBLBC = downloadFile(url_kfz_house_IFC4, "url_kfz_house_IFC4Test")
        val fileOptimizedBLBC =
            BIMFileOptimizer.optimizeIfcFile(
                downloadFile(url_kfz_house_IFC4_BC_BL, "url_kfz_house_IFC4_BC_BLTest"),
                optimizeInput_RBC = true,
                optimizeInput_RBL = true
            )
        val referenceBL = fileReferenceBLBC.useLines { it.toList() }
        val optimizedBL = fileOptimizedBLBC.useLines { it.toList() }
        // check if entries equal
        Assertions.assertTrue(referenceBL.zip(optimizedBL).all { (x, y) -> x == y })

        // clean up test directory
        cleanTestDirectory()
    }

    // helper

    @Throws(IOException::class)
    private fun downloadFile(url: URL, tmpFileName: String): File {
        // check if test directory already exists, if not create
        val directoryPath = "${System.getProperty("user.dir")}/src/test/output/tmp_test".replace("/", File.separator)
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdir()
        }
        // download file into test directory
        val tmpFile = File("$directoryPath${File.separator}$tmpFileName")
        try {
            val bytes = url.readBytes()
            tmpFile.writeBytes(bytes)
        } catch (e: IOException) {
            throw IOException("Could not download file $url. Abort test.")
        }
        return tmpFile
    }

    private fun cleanTestDirectory(){
        val directoryPath = "${System.getProperty("user.dir")}/src/test/output/tmp_test".replace("/", File.separator)
        File(directoryPath).deleteRecursively()
    }

}