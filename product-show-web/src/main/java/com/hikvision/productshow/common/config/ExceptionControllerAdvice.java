package com.hikvision.productshow.common.config;

import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.enums.CommonErrorCode;
import com.hikvision.productshow.common.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Autowired
    MultipartProperties multipartProperties;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public BaseResult<String> paramsMissingHandler(MissingServletRequestParameterException e) {
        String msg = CommonErrorCode.PARAMS_BLANK.getMsg().replace("$$", e.getParameterName());
        return new BaseResult(CommonErrorCode.PARAMS_BLANK.getCode(), msg, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public BaseResult<String> messageNotReadableHandler(HttpMessageNotReadableException e) {
        return new BaseResult(CommonErrorCode.PARAMS_HTTP_MESSAGE_NOT_READABLE.getCode(), CommonErrorCode.PARAMS_HTTP_MESSAGE_NOT_READABLE.getMsg(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResult<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = this.getErrorMsg(e.getBindingResult());
        return new BaseResult(CommonErrorCode.SYSTEM_PARAMETER_ILLEGAL_ERROR.getCode(), CommonErrorCode.SYSTEM_PARAMETER_ILLEGAL_ERROR.getMsg(), errorMap);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public BaseResult<Map<String, String>> bindExceptionHandler(BindException e) {
        Map<String, String> errorMap = this.getErrorMsg(e.getBindingResult());
        return new BaseResult(CommonErrorCode.SYSTEM_PARAMETER_ILLEGAL_ERROR.getCode(), CommonErrorCode.SYSTEM_PARAMETER_ILLEGAL_ERROR.getMsg(), errorMap);
    }

    @ExceptionHandler({ApiException.class})
    public BaseResult apiExceptionHandler(ApiException e) {
        return BaseResult.fail(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public BaseResult<String> methodNotSupportedHandler(HttpRequestMethodNotSupportedException e) {
        return new BaseResult(CommonErrorCode.NETWORK_NOT_ALLOW_METHOD.getCode(), CommonErrorCode.NETWORK_NOT_ALLOW_METHOD.getMsg(), "");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public BaseResult<Map<String, String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap(16);
        Iterator var3 = e.getConstraintViolations().iterator();
        while (var3.hasNext()) {
            ConstraintViolation<?> error = (ConstraintViolation) var3.next();
            String code = error.getPropertyPath().toString();
            code = StringUtils.substringAfter(code, ".");
            String msg = error.getMessage();
            errorMap.put(code, msg);
        }
        return new BaseResult(CommonErrorCode.SYSTEM_PARAMETER_ILLEGAL_ERROR.getCode(), CommonErrorCode.SYSTEM_PARAMETER_ILLEGAL_ERROR.getMsg(), errorMap);
    }

    /**
     * @Description: 数据库异常
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public BaseResult dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        String msg;
        if (e.getCause().getCause() != null) {
            //jpa走这里
            msg = e.getCause().getCause().getLocalizedMessage();
            if (StringUtils.isNotBlank(msg)) {
                msg = msg.substring(msg.indexOf("详细") + 3);
            }
        } else {
            //mybatis plus
            msg = e.getCause().getLocalizedMessage();
        }
        return BaseResult.fail(CommonErrorCode.DB_SAVE_ERROR.getCode(), CommonErrorCode.DB_SAVE_ERROR.getMsg(), msg);
    }

    /**
     * @Description: 要删除的id不存在
     */
    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public BaseResult dataIntegrityViolationExceptionHandler(EmptyResultDataAccessException e) {
        return BaseResult.fail(CommonErrorCode.DELETE_INVALID.getCode(), CommonErrorCode.DELETE_INVALID.getMsg());
    }

    /**
     * 上传文件超过，捕获异常：MaxUploadSizeExceededException
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return BaseResult.fail(CommonErrorCode.UPLOAD_SIZE_INVALID.getCode(),
                String.format(CommonErrorCode.UPLOAD_SIZE_INVALID.getMsg(),
                        multipartProperties.getMaxFileSize().toBytes() / 1024.0,
                        multipartProperties.getMaxRequestSize().toBytes() / 1024.0));
    }

    /**
     * 文件找不到
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(FileNotFoundException.class)
    public BaseResult handlerFileNotFoundException(FileNotFoundException ex) {
        return BaseResult.fail(CommonErrorCode.FILE_NOT_EXISTS.getCode(),
                String.format(CommonErrorCode.FILE_NOT_EXISTS.getMsg()));
    }

    @ExceptionHandler(Exception.class)
    public BaseResult otherException(Exception e) {
        // 处理SQLException
        if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof SQLException) {
            return BaseResult.fail(CommonErrorCode.OTHER_ERROR.getCode(), CommonErrorCode.OTHER_ERROR.getMsg(),
                    handlerSqlException((SQLException) e.getCause().getCause()));
        }
        e.printStackTrace();
        return BaseResult.fail(CommonErrorCode.OTHER_ERROR.getCode(), CommonErrorCode.OTHER_ERROR.getMsg(), e.getMessage());
    }

    /**
     * 处理SQLException。方法参考的 SqlExceptionHelper.logExceptions
     *
     * @param sqlException
     * @return
     */
    Object handlerSqlException(SQLException sqlException) {
        List<String> previousErrorMessages = new ArrayList<>();
        while (sqlException != null) {
            if (!previousErrorMessages.contains(sqlException.getMessage())) {
                previousErrorMessages.add(sqlException.getMessage());
            }
            sqlException = sqlException.getNextException();
        }
        return previousErrorMessages;
    }

    private Map<String, String> getErrorMsg(BindingResult bindingResult) {
        Map<String, String> errorMap = new HashMap(16);
        Iterator var3 = bindingResult.getAllErrors().iterator();

        while (var3.hasNext()) {
            ObjectError error = (ObjectError) var3.next();
            String msg = error.getDefaultMessage();
            String objectName = StringUtils.trim(error.getObjectName());
            if (StringUtils.isNotBlank(objectName)) {
                objectName = objectName + ".";
            }

            String field;
            if (error instanceof FieldError) {
                field = StringUtils.trim(((FieldError) error).getField());
                if (StringUtils.isNotBlank(field)) {
                    field = field + ".";
                }

                errorMap.put(objectName + field, msg);
            } else {
                field = error.getCode();
                errorMap.put(objectName + field, msg);
            }
        }

        return errorMap;
    }
}
