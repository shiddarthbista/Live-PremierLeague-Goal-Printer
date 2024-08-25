package bista.shiddarth.live_pl_goal_printer.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GoalEvent(
    val homeTeam: String,
    val homeScore: Int,
    val homeGoalScorers : List<String>,
    val awayTeam: String,
    val awayScore: Int,
    val awayGoalScorers : List<String>,
    val goalScorer: String,
    val assist: String?,
    val minute: Int,
    val hasHomeTeamScored: Boolean
)

data class PlayerMap(
    val elements : List<Element>
)

data class Element(
    val id : Int,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("second_name")
    val lastName: String,

)
