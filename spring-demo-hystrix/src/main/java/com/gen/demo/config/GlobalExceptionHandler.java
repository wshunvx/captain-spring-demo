package com.gen.demo.config;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.netflix.eureka.command.CommandResponse;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommandResponse<?> otherMaxUploadSizeExceeded(HttpServletResponse response, Exception ex) {
        response.setStatus(500);
        logger.error(ex.getMessage());
        return CommandResponse.ofFailure(9999, "MaxUploadSize:请联系管理员！");
    }
	
	@ExceptionHandler(BlockException.class)
    public CommandResponse<?> otherBlockHandler(HttpServletResponse response, Exception ex) {
		response.setStatus(500);
		logger.error("Blocked by Security: {}", ex.getMessage());
        return CommandResponse.ofFailure(9999, "BlockException:请联系管理员！");
    }
	
//	@ExceptionHandler(Exception.class)
//    public CommandResponse<?> otherExceptionHandler(HttpServletResponse response, Exception ex) {
//        response.setStatus(500);
//        logger.error(ex.getMessage());
//        return CommandResponse.ofFailure(9999, "ServiceError:请联系管理员！");
//    }

}
