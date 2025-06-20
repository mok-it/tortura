package mok.it.tortura

import kotbase.*
import mok.it.tortura.model.Competition
import mok.it.tortura.util.appDirs
import mok.it.tortura.util.mapJsonFormat
import mok.it.tortura.util.toJson

object CompetitionDb {

    private const val DATABASE_NAME = "tortura_db"
    private const val COLLECTION_NAME = "competitions"

    init{
        initCBL()
    }

    val database = Database(
        DATABASE_NAME,
        DatabaseConfigurationFactory.newConfig( appDirs.getUserDataDir() )
    )



    val collection
        get() = database.createCollection(COLLECTION_NAME)

    fun saveCompetition(competition: Competition) {
        val document = MutableDocument(competition.id, competition.toJson())
        collection.save(document)
    }

    fun getCompetitions(): List<Competition> {
        val query = QueryBuilder.select(
            SelectResult.property("teamAssignment"),
            SelectResult.property("problemSet"),
            SelectResult.property("answers"),
            SelectResult.property("startTime"),
            SelectResult.expression(Meta.id),
        )
            .from(DataSource.collection(collection))

        val competitions = mutableListOf<Competition>()

        query.execute().use { results ->
            results.forEach {
                val competition = mapJsonFormat.decodeFromString<Competition>(it.toJSON())

                competitions.add(competition)
            }
        }
        return competitions
    }

    val isEmpty
        get() = getCompetitions().isEmpty()

    private fun clearDatabase() {
        database.deleteCollection(COLLECTION_NAME)
    }

    fun overwriteDatabase(competitions: List<Competition>) {
        clearDatabase()
        competitions.forEach { saveCompetition(it) }
    }
}