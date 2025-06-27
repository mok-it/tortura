package mok.it.tortura

import com.github.doyaaaaaken.kotlincsv.dsl.context.InsufficientFieldsRowBehaviour
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import mok.it.tortura.model.*
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileInputStream

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val excelImportImplemented: Boolean = true
}

actual fun getPlatform(): Platform = JVMPlatform()

actual suspend fun saveStringToFile(file: PlatformFile, string: String) {
    file.writeString(string)
}

actual fun goodNightGoodBye() {
    System.exit(0)
}

actual fun loadProblemSetFromExcel(file: PlatformFile): ProblemSet? {

    data class ProblemSetExcelRow(
        val number: Int,
        val text: String,
        val solution: String,
    )

    try {
        val inputStream = FileInputStream(file.path)
        val workBook = WorkbookFactory.create(inputStream)

        val sheet = workBook.getSheetAt(0)

        val rows = mutableListOf<ProblemSetExcelRow>()

        for (i in 1..10) {
            val row = sheet.getRow(i)

            rows.add(
                ProblemSetExcelRow(
                    number = row.getCell(0).numericCellValue.toInt(),
                    text = row.getCell(1).stringCellValue,
                    solution = row.getCell(2).stringCellValue
                )
            )
        }

        rows.sortBy { it.number }

        val blocks = mutableListOf<Block>()
        var used = 0

        for (i in 4 downTo 1) {
            val tasks = mutableListOf<Task>()
            for (j in used..<used + i) {
                tasks.add(Task(text = rows[j].text, solution = rows[j].solution))
            }
            blocks.add(
                Block(
                    tasks = tasks,
                    minCorrectToProgress = (tasks.size + 2) / 2,
                )
            )
            used += i
        }
        return ProblemSet(
            name = "Imported from Excel",
            blocks = blocks,
        )
    } catch (_: Exception) {
        return null
    }
}



actual fun loadTeamAssignmentFromExcel(file: PlatformFile): TeamAssignment? {
    val inputStream = FileInputStream(file.path)
    val workBook = WorkbookFactory.create(inputStream)

    val sheet = workBook.getSheetAt(0)

    val teams = mutableListOf<Team>()

    TODO("Formátumot ki kell még találni")
}


actual suspend fun loadTeamAssignmentFromCsv(file: PlatformFile): TeamAssignment? {
    val csvData = file.readString()
    val rows = csvReader{
        insufficientFieldsRowBehaviour = InsufficientFieldsRowBehaviour.EMPTY_STRING
    }.readAll(csvData)

    val teams = mutableListOf<Team>()
    for( row in rows ){
        val students = mutableListOf<Student>()
        for( i in 1 ..< row.size ){
            if( row[ i ] != "" ) {
                students.add(Student( name = row[ i ] ) )
            }

        }
        teams.add(
            Team(
                students = students,
                name = row[0],
            )
        )
    }
    return TeamAssignment(
        category = "",
        teams = teams,
    )
}