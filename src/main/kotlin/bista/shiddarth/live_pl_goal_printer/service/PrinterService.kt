package bista.shiddarth.live_pl_goal_printer.service

import bista.shiddarth.live_pl_goal_printer.model.GoalEvent
import org.springframework.stereotype.Service
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.print.PageFormat
import java.awt.print.Printable
import java.awt.print.PrinterException
import javax.print.*
import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.PrintRequestAttributeSet

@Service
class PrinterService : Printable {

    val printers: List<String>
        get() {
            val flavor: DocFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE
            val pras: PrintRequestAttributeSet = HashPrintRequestAttributeSet()

            val printServices = PrintServiceLookup.lookupPrintServices(
                flavor, pras
            )

            val printerList: MutableList<String> = ArrayList()
            for (printerService in printServices) {
                printerList.add(printerService.name)
            }

            return printerList
        }

    @Throws(PrinterException::class)
    override fun print(g: Graphics, pf: PageFormat, page: Int): Int {
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return Printable.NO_SUCH_PAGE
        }

        /*
         * User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        val g2d = g as Graphics2D
        g2d.translate(pf.imageableX, pf.imageableY)

        /* Now we perform our rendering */
        g.setFont(Font("Roman", 0, 8))
        g.drawString("Hello world !", 0, 10)

        return Printable.PAGE_EXISTS
    }

    fun printString(text: String) {
        // find the printService of name printerName

        val flavor: DocFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE
        val pras: PrintRequestAttributeSet = HashPrintRequestAttributeSet()

        val printService = PrintServiceLookup.lookupPrintServices(
            flavor, pras
        )
        val service = printService.first{
            it.name == PRINTER_NAME
        }
        println(service.toString())

        val job = service.createPrintJob()

        try {
            // important for umlaut chars
            val bytes = text.toByteArray(charset("CP437"))

            val doc: Doc = SimpleDoc(bytes, flavor, null)


            job.print(doc, null)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    fun printBytes(bytes: ByteArray?) {
        val flavor: DocFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE
        val pras: PrintRequestAttributeSet = HashPrintRequestAttributeSet()

        val printService = PrintServiceLookup.lookupPrintServices(
            flavor, pras
        )
        val service = printService.first{
            it.name == PRINTER_NAME
        }

        val job = service.createPrintJob()

        try {
            val doc: Doc = SimpleDoc(bytes, flavor, null)

            job.print(doc, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun String.shortName(minute: Int) : String {
        val parts = this.split(" ")
        return try {
            "${parts[0].first()}.${parts[1]} (${minute}`)"
        } catch (e : Exception) {
            return this
        }
    }

    fun printGoalEvent(goalEvent: GoalEvent){
        // Define the total width of the line
        val totalWidth = 48  // Adjust this to fit your printer's line width

        val homeGoalScorerWidth = 20 // Adjust this as needed

        val awayGoalScorerWidth = totalWidth - homeGoalScorerWidth

        val homeGoalScorer = if(goalEvent.hasHomeTeamScored) goalEvent.goalScorer.shortName(goalEvent.minute) else ""
        val awayGoalScorer = if(!goalEvent.hasHomeTeamScored) goalEvent.goalScorer.shortName(goalEvent.minute) else ""
        val homeAssist: String = if(goalEvent.hasHomeTeamScored) goalEvent.assist ?: "" else ""
        val awayAssist: String = if(!goalEvent.hasHomeTeamScored) goalEvent.assist ?: "" else ""


        val goal = String.format("%-${homeGoalScorerWidth}s%${awayGoalScorerWidth}s", homeGoalScorer, awayGoalScorer)
        val assist = String.format("%-${homeGoalScorerWidth}s%${awayGoalScorerWidth}s", homeAssist, awayAssist)
        val goalEventText = """
            
                             GOALLLLLLL!!!!!!
            ${goalEvent.homeTeam} - ${goalEvent.homeScore}                                  ${goalEvent.awayScore} - ${goalEvent.awayTeam}
            $goal
            $assist
            
            
            
            
            
        """.trimIndent()
        println(goalEventText)
        printString(goalEventText)
        val cutP = byteArrayOf(0x1d, 'V'.code.toByte(), 1)

        printBytes( cutP)
    }

    companion object {
        const val PRINTER_NAME = "EPSON TM-T20III Receipt"
    }
}