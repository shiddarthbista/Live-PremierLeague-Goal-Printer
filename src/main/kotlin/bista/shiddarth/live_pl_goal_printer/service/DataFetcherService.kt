package bista.shiddarth.live_pl_goal_printer.service

import bista.shiddarth.live_pl_goal_printer.model.DataMap
import bista.shiddarth.live_pl_goal_printer.model.FplEvent
import bista.shiddarth.live_pl_goal_printer.model.GoalEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DataFetcherService(private val fplService: FplService) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val latestScoresMap = mutableMapOf<Int, Pair<Int, Int>>()

    fun getGoalsScored(): List<GoalEvent> {
        log.info("Getting Latest stats from FPL")
        val latestEvents = fplService.getLatestEvents().filter { it.started && !it.finishedProvisional }
        log.info(latestEvents.toString())

        return latestEvents.filter {
            hasAnyTeamScored(it)
        }.map { event ->
            val hasHomeTeamScored = event.homeTeamScore > (latestScoresMap[event.matchId]?.first ?: 0)
            //Update with latest scores
            latestScoresMap[event.matchId] = Pair(event.homeTeamScore, event.awayTeamScore)

            val latestGoalStat = event.stats.find { it.identifier == "goals_scored" }
            val latestAssistStat = event.stats.find { it.identifier == "assists" }

            val goalScorer = if (hasHomeTeamScored) {
                DataMap.playerIdMap[latestGoalStat?.homeTeamStats?.lastOrNull()?.element]
            } else {
                DataMap.playerIdMap[latestGoalStat?.awayTeamStats?.lastOrNull()?.element]
            }

            val assist = if (hasHomeTeamScored) {
                DataMap.playerIdMap[latestAssistStat?.homeTeamStats?.lastOrNull()?.element]
            } else {
                DataMap.playerIdMap[latestAssistStat?.awayTeamStats?.lastOrNull()?.element]
            }

            GoalEvent(
                homeTeam = DataMap.teamIdMap.getValue(event.homeTeam),
                homeScore = event.homeTeamScore,
                homeGoalScorers = event.stats.find { it.identifier == "goals_scored" }?.homeTeamStats?.map {
                    DataMap.playerIdMap.getValue(
                        it.element
                    )
                } ?: emptyList(),
                awayTeam = DataMap.teamIdMap[event.awayTeam] ?: "No data found",
                awayScore = event.awayTeamScore,
                awayGoalScorers = event.stats.find { it.identifier == "goals_scored" }?.awayTeamStats?.map {
                    DataMap.playerIdMap.getValue(
                        it.element
                    )
                } ?: emptyList(),
                goalScorer = goalScorer ?: "No goal scorer found",
                assist = assist ?: "No assist found",
                minute = event.minutes
            )
        }

    }

    private fun hasAnyTeamScored(event: FplEvent): Boolean {
        val latestScore = latestScoresMap[event.matchId] ?: Pair(0, 0)
        val hasHomeTeamScored = event.homeTeamScore > latestScore.first
        val hasAwayTeamScored = event.awayTeamScore > latestScore.second

        return hasHomeTeamScored || hasAwayTeamScored
    }
}