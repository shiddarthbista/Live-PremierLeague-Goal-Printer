package bista.shiddarth.live_pl_goal_printer.client

import bista.shiddarth.live_pl_goal_printer.model.FplEvent
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class FplClient {
    private final val restClient: RestClient = RestClient.create()

    private val realUrl = "https://fantasy.premierleague.com/api/fixtures/?event=1"
    private val mockUrl = "http://localhost:8081/score"

    fun getAllEvents(): List<FplEvent> {
        val events = restClient.get()
            .uri(realUrl)
            .retrieve()
            .body(object : ParameterizedTypeReference<List<FplEvent>>() {})

        return events ?: emptyList()
    }


}