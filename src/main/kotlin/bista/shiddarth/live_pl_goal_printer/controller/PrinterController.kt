package bista.shiddarth.live_pl_goal_printer.controller

import bista.shiddarth.live_pl_goal_printer.model.FplEvent
import bista.shiddarth.live_pl_goal_printer.model.GoalEvent
import bista.shiddarth.live_pl_goal_printer.service.DataFetcherService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class PrinterController(private val dataFetcherService: DataFetcherService) {
    val restClient = RestClient().create

    @GetMapping("/goals")
    fun getGoalScored(): List<GoalEvent> {
        return dataFetcherService.getGoalsScored()
    }

    @GetMapping("/players")
    fun getPlayers() {

    }

}