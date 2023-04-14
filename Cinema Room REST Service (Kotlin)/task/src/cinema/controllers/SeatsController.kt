package cinema.controllers

import cinema.data.*
import cinema.exceptions.SeatsAccessException
import cinema.exceptions.SeatsException
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
class SeatsController {
    private val seats: Seats
    private val purchased = mutableMapOf<String, Seat>()

    init {
        val rows = 9
        val cols = 9
        val listSeat = mutableListOf<Seat>()
        for (row in 1..rows) {
            for (col in 1..cols) {
                listSeat.add(Seat(row, col, if (row <= 4) 10 else 8))
            }
        }
        seats = Seats(rows, cols, listSeat.toList())
    }

    @GetMapping("/seats")
    fun getSeats(): Seats {
        return seats
    }

    @PostMapping("/purchase")
    fun purchaseSeat(@RequestBody purchaseSeatRequest: PurchaseSeatRequest): PurchaseSeatResponse {
        if (purchaseSeatRequest.column !in 1..seats.total_columns) throw SeatsException("The number of a row or a column is out of bounds!")
        if (purchaseSeatRequest.row !in 1..seats.total_rows) throw SeatsException("The number of a row or a column is out of bounds!")
        val seat =
            seats.available_seats.find { it.column == purchaseSeatRequest.column && it.row == purchaseSeatRequest.row }
        seats.available_seats =
            seats.available_seats.minus(seat ?: throw SeatsException("The ticket has been already purchased!"))
        val res = PurchaseSeatResponse(UUID.randomUUID().toString(), seat)
        purchased[res.token] = res.ticket
        return res
    }

    @PostMapping("/return")
    fun returnSeat(@RequestBody returnSeatRequest: ReturnSeatRequest): ReturnSeatResponse {
        val seat = purchased[returnSeatRequest.token] ?: throw SeatsException("Wrong token!")
        seats.available_seats = seats.available_seats.plus(seat)
        purchased.remove(returnSeatRequest.token)
        return ReturnSeatResponse(seat)
    }

    @PostMapping(
        value = ["/stats"],
        //consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON]
    )

    fun returnStats(@RequestParam statsRequest: Map<String, String>?): SeatStatsResponse {
        if (statsRequest == null || statsRequest.get("password") != "super_secret") throw SeatsAccessException("The password is wrong!")
        return SeatStatsResponse(seats.available_seats.size, purchased.values.sumOf { it.price }, purchased.size)
    }
}