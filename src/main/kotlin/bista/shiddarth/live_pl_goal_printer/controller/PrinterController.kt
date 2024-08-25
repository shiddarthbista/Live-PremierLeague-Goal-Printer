package bista.shiddarth.live_pl_goal_printer.controller

import bista.shiddarth.live_pl_goal_printer.model.GoalEvent
import bista.shiddarth.live_pl_goal_printer.model.PlayerMap
import bista.shiddarth.live_pl_goal_printer.service.DataFetcherService
import bista.shiddarth.live_pl_goal_printer.service.PrinterService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient


@RestController
class PrinterController(private val dataFetcherService: DataFetcherService,private val printerService: PrinterService) {
    private final val restClient: RestClient = RestClient.create()
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 60000)
    @GetMapping("/goals")
    fun getGoalScored(): List<GoalEvent> {
        val goalsScored = dataFetcherService.getGoalsScored()
        if (goalsScored.isNotEmpty()) {
            logger.info(goalsScored.toString())
            goalsScored.forEach {
                printerService.printGoalEvent(it)
            }
        }
        return goalsScored
    }

    @GetMapping("/players")
    fun getPlayers(): List<String> {
        println("CALLING ")
        val result = restClient.get()
            .uri("https://fantasy.premierleague.com/api/bootstrap-static/")
            .retrieve()
            .body(PlayerMap::class.java)

        return result?.elements?.map {
            "${it.id} to ${it.firstName} ${it.lastName}"
        } ?: emptyList()
    }

}

