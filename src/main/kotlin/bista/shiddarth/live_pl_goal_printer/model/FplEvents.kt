package bista.shiddarth.live_pl_goal_printer.model

import com.fasterxml.jackson.annotation.JsonProperty

data class FplEvent(
    @JsonProperty("code")
    val matchId: Int,
    @JsonProperty("finished_provisional")
    val finishedProvisional: Boolean,
    val minutes: Int,
    val started: Boolean,
    @JsonProperty("team_a")
    val awayTeam : Int,
    @JsonProperty("team_a_score")
    val awayTeamScore: Int,
    @JsonProperty("team_h")
    val homeTeam : Int,
    @JsonProperty("team_h_score")
    val homeTeamScore: Int,
    val stats: List<GameStats>
)

data class GameStats(
    val identifier: String,
    @JsonProperty("h")
    val homeTeamStats: List<StatDetail>,
    @JsonProperty("a")
    val awayTeamStats: List<StatDetail>
)

data class StatDetail(
    val value:Int,
    val element: Int
)
