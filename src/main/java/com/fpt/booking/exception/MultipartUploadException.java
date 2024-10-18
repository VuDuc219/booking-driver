package com.fpt.booking.exception;


import com.fpt.booking.config.ResourceBundleConfig;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;



@RestControllerAdvice
@RequiredArgsConstructor
public class MultipartUploadException {

    private final ResourceBundleConfig resourceBundleConfig;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handlerFileUpload(MaxUploadSizeExceededException exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        return this.resourceBundleConfig.getViMessage(MessageUtils.MAX_SIZE_FILE);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        return ResponseEntity.ok(MessageResponse.builder().message(this.resourceBundleConfig.getViMessage(MessageUtils.LOGIN_USER_APP)).build());
    }

    @ExceptionHandler({ BadRequestException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.ok(MessageResponse.builder().message(this.resourceBundleConfig.getViMessage(MessageUtils.FORBIDDEN)).build());
    }
}
