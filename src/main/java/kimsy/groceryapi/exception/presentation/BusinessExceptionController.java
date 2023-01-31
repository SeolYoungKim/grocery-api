package kimsy.groceryapi.exception.presentation;

import kimsy.groceryapi.exception.presentation.dto.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class BusinessExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String illegalArgsHandler(RuntimeException e, Model model) {
        log.info("[GroceryAPI 404 ERROR] message={}", e.getMessage());

        final ExceptionResult exceptionResult = new ExceptionResult(HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(), e.getMessage());

        model.addAttribute("exceptionResult", exceptionResult);
        return "/error/4xx";
    }
}
