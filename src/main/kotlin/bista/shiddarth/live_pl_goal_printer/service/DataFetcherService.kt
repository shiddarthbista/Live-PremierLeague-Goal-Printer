package bista.shiddarth.live_pl_goal_printer.service

import bista.shiddarth.live_pl_goal_printer.model.FplEvent
import bista.shiddarth.live_pl_goal_printer.model.GoalEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DataFetcherService(private val fplService: FplService) {

    val log = LoggerFactory.getLogger(this::class.java)
    fun getGoalsScored(): List<GoalEvent> {
        val latestEvents = fplService.getLatestEvents()
        log.info(latestEvents.toString())

        return latestEvents.filter { it.finishedProvisional }. map { event ->
            GoalEvent(
                homeTeam = event.homeTeam.toString(),
                awayTeam = event.awayTeam.toString(),
                homeScore = event.homeTeamScore,
                awayScore = event.awayTeamScore,
                goalScorer = event.stats.first { gameStats -> gameStats.identifier == "goals_scored" }.homeTeamStats.firstNotNullOfOrNull { it.element.toString() }
                    ?: " No Goal scored",
                assist = event.stats.first { gameStats -> gameStats.identifier == "assists" }.homeTeamStats.firstNotNullOfOrNull { it.element.toString() }
                    ?: " No Assist given"
            )
        }

    }
}