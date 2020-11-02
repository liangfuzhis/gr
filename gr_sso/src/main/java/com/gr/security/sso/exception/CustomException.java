package com.gr.security.sso.exception;

import com.gr.security.sso.common.ResultJson;
import lombok.Getter;

/**
 * @author lfz
 * @since  2020.09.30
 */

@Getter
public class CustomException extends RuntimeException{
    private ResultJson resultJson;

    public CustomException(ResultJson resultJson) {
        this.resultJson = resultJson;
    }
}
