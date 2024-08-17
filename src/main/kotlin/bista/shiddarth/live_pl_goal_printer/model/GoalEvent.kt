package bista.shiddarth.live_pl_goal_printer.model

data class GoalEvent(
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val goalScorer: String,
    val assist: String?
)
