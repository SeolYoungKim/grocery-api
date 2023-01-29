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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public String notFountHandler(Model model) {
        final ExceptionResult exceptionResult = new ExceptionResult(HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.value(), "없는 품목입니다. 전체 목록의 품목명을 확인해 주세요.");

        model.addAttribute("exceptionResult", exceptionResult);
        return "/error/4xx";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebClientResponseException.BadRequest.class)
    public String badRequestHandler(WebClientResponseException e, Model model) {
        log.info("외부 API에서 예외 발생 : {}", e.getResponseBodyAs(String.class));

        final ExceptionResult exceptionResult = new ExceptionResult(HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.");

        model.addAttribute("exceptionResult", exceptionResult);
        return "/error/4xx";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(WebClientResponseException.InternalServerError.class)
    public String internalServerErrorHandler(WebClientResponseException e , Model model) {
        e.getMessage();
        return "";
    }
}
