package cinema.data

data class Seat(val row: Int, val column: Int, val price: Int)

data class Seats(
    val total_rows: Int,
    val total_columns: Int,
    var available_seats: List<Seat> = listOf()
)

data class PurchaseSeatRequest(
    val row: Int,
    val column: Int,
)

data class PurchaseSeatResponse(
    val token: String,
    val ticket: Seat,
)

data class ReturnSeatRequest(val token: String? = null)
data class ReturnSeatResponse(val returned_ticket: Seat)
data class SeatErrorResponse(val error: String? = null)

data class SeatStatsRequest(val password: String? = null)
data class SeatStatsResponse(
    val number_of_available_seats: Int,
    val current_income: Int = 0,
    val number_of_purchased_tickets: Int = 0
)

