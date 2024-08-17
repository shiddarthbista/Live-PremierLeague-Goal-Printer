package bista.shiddarth.live_pl_goal_printer.service

import bista.shiddarth.live_pl_goal_printer.client.FplClient
import bista.shiddarth.live_pl_goal_printer.model.FplEvent
import org.springframework.stereotype.Service

@Service
class FplService(private val fplClient: FplClient) {
    fun getLatestEvents(): List<FplEvent> {
        return fplClient.getAllEvents()
    }
}