package cinema.configuration

import cinema.data.SeatErrorResponse
import cinema.exceptions.SeatsAccessException
import cinema.exceptions.SeatsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler
    fun handleException(exc: SeatsException): ResponseEntity<SeatErrorResponse> {
        val seatErrorResponse = SeatErrorResponse(exc.message)
        return ResponseEntity(seatErrorResponse, HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler
    fun handleAccessException(exc: SeatsAccessException): ResponseEntity<SeatErrorResponse> {
        val seatErrorResponse = SeatErrorResponse(exc.message)
        return ResponseEntity(seatErrorResponse, HttpStatus.UNAUTHORIZED)
    }
}