package de.rebsc.bimtoosm.loader

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
import org.apache.commons.io.IOUtils
import org.bimserver.emf.IfcModelInterface
import org.bimserver.emf.PackageMetaData
import org.bimserver.emf.Schema
import org.bimserver.ifc.step.deserializer.DetectIfcVersion
import org.bimserver.ifc.step.deserializer.Ifc4StepDeserializer
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Package
import org.bimserver.models.ifc4.Ifc4Package
import org.eclipse.emf.ecore.EPackage
import java.io.*
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths

class Loader {

    companion object {

        /**
         * Loads ifc file into model
         * @param filepath to ifc file
         * @return ifc model holding ifc objects
         * @throws BIMtoOSMException
         */
        fun loadIntoModel(filepath: String): IfcModelInterface {

            try {
                // detect ifc schema
                val path = Paths.get(filepath)
                val head = ByteArray(4096)
                IOUtils.readFully(Files.newInputStream(path), head)
                val schemaString = DetectIfcVersion().detectVersion(head, false)

                var schema = Schema.IFC2X3TC1   // default
                var ePackage: EPackage = Ifc2x3tc1Package.eINSTANCE // default
                when (schemaString) {
                    Schema.IFC4.headerName -> {
                        schema = Schema.IFC4
                        ePackage = Ifc4Package.eINSTANCE
                    }
                    Schema.IFC2X3TC1.headerName -> {
                        schema = Schema.IFC2X3TC1
                        ePackage = Ifc2x3tc1Package.eINSTANCE
                    }
                }

                // deserialize
                // for now use more stable deprecated IFc4StepDeserializer instead of Ifc4StepStreamingDeserializer
                val deserializer = Ifc4StepDeserializer(schema)
                val packageMetaData = PackageMetaData(ePackage, schema, Paths.get("tmp"))
                deserializer.init(packageMetaData)
                val openStream = File(filepath).inputStream()
                val baos = ByteArrayOutputStream()
                IOUtils.copy(openStream, baos)
                val model = deserializer.read(ByteArrayInputStream(baos.toByteArray()), "", baos.size().toLong(), null)

                if (!model.isValid) {
                    throw BIMtoOSMException("Ifc model invalid")
                }
                if (model.objects.isEmpty()) {
                    throw BIMtoOSMException("Ifc model empty")
                }

                openStream.close()
                return model
            } catch (e: Exception) {
                when (e) {
                    is FileNotFoundException, is NoSuchFileException -> throw BIMtoOSMException("File not found: $filepath")
                    is NullPointerException -> throw BIMtoOSMException("Invalid IFC Schema file: $filepath")
                    else -> throw e
                }
            }

        }
    }

}