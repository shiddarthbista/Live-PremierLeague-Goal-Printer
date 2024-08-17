package bista.shiddarth.live_pl_goal_printer.model

data class GoalEvent(
    val homeTeam: String,
    val homeScore: Int,
    val homeGoalScorers : List<String>,
    val awayTeam: String,
    val awayScore: Int,
    val awayGoalScorers : List<String>,
    val goalScorer: String,
    val assist: String?
)
