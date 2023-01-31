package kimsy.groceryapi.exception.presentation;

import kimsy.groceryapi.exception.presentation.dto.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@ControllerAdvice
public class WebClientExceptionController {
    private static final String EXCEPTION_RESULT = "exceptionResult";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public String notFountHandler(WebClientResponseException e, Model model) {
        log.info("[API 404 ERROR] message={}", e.getMessage());
        log.info("[API 404 ERROR] response body={}", e.getResponseBodyAs(String.class));

        final ExceptionResult exceptionResult = new ExceptionResult(HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.value(), "없는 품목입니다. 전체 목록의 품목명을 확인해 주세요.");

        model.addAttribute(EXCEPTION_RESULT, exceptionResult);
        return "/error/4xx";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebClientResponseException.BadRequest.class)
    public String badRequestHandler(WebClientResponseException e, Model model) {
        log.info("[API 400 ERROR] message={}", e.getMessage());
        log.info("[API 400 ERROR] response body={}", e.getResponseBodyAs(String.class));

        final ExceptionResult exceptionResult = new ExceptionResult(HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.");

        model.addAttribute(EXCEPTION_RESULT, exceptionResult);
        return "/error/4xx";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(WebClientResponseException.InternalServerError.class)
    public String internalServerErrorHandler(WebClientResponseException e) {
        log.info("[API 500 ERROR] message={}", e.getMessage());
        log.info("[API 500 ERROR] response body={}", e.getResponseBodyAs(String.class));

        return "/error/5xx";
    }
}
