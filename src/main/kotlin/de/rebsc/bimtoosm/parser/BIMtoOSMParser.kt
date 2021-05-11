package de.rebsc.bimtoosm.parser

import de.rebsc.bimtoosm.api.BIMtoOSM
import de.rebsc.bimtoosm.data.OSMDataSet
import de.rebsc.bimtoosm.exception.BIMtoOSMException
import nl.tue.buildingsmart.express.population.ModelPopulation
import java.io.File
import java.nio.file.Paths

class BIMtoOSMParser(private var config: Configuration) : BIMtoOSM {

    private lateinit var file : File
    private lateinit var ifcModel : ModelPopulation

    override fun configure(config: Configuration) {
        this.config = config
    }

    override fun parse(filepath: String): OSMDataSet {
        loadFile(filepath)
        // TODO implement

        return OSMDataSet()
    }

    /**
     * Loads ifc file into model
     * @throws BIMtoOSMException
     */
    private fun loadFile(filepath: String){
        try {
            file = File(filepath)
            val inputFs = file.inputStream()
            ifcModel = ModelPopulation(inputFs)

            // TODO fix - implementation for testing only
            val ifcSchemaFilePath = System.getProperty("user.dir") + "/src/main/resources/IFC4.exp"
            ifcModel.schemaFile = Paths.get(ifcSchemaFilePath)
            ifcModel.load()
            inputFs.close()

        } catch (e: NullPointerException){
            throw BIMtoOSMException("Could not load $filepath")
        }
    }
}