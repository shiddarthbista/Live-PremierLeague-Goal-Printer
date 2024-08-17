package bista.shiddarth.live_pl_goal_printer.service

import bista.shiddarth.live_pl_goal_printer.model.DataMap
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
                homeTeam = DataMap.teamIdMap.getValue(event.homeTeam),
                homeScore = event.homeTeamScore,
                homeGoalScorers = event.stats.find {it.identifier == "goals_scored"}?.homeTeamStats?.map { it.element.toString() } ?: emptyList(),
                awayTeam = DataMap.teamIdMap.getValue(event.awayTeam),
                awayScore = event.awayTeamScore,
                awayGoalScorers = event.stats.find {it.identifier == "goals_scored"}?.awayTeamStats?.map { it.element.toString() } ?: emptyList(),
                goalScorer = event.stats.first { gameStats -> gameStats.identifier == "goals_scored" }.homeTeamStats.firstNotNullOfOrNull { it.element.toString() }
                    ?: " No Goal scored",
                assist = event.stats.first { gameStats -> gameStats.identifier == "assists" }.homeTeamStats.firstNotNullOfOrNull { it.element.toString() }
                    ?: " No Assist given"
            )
        }

    }
}