package com.mylo.spring.service.sample.validate;


import com.mylo.common.core.exception.MyloCommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import static com.mylo.common.core.exception.ErrorType.ERROR_SAMPLE;
import static com.mylo.common.core.exception.ServiceStatusCode.ERROR_PARAM_VALIDITY;

@Slf4j
@Service
public class SampleValidate {

    public void paramValidation(Integer idx, String keyword, int pageNum, int pageSize) {
        //ToDo 꼭 필요한 값이 아닌경우는 유효성체크 안해도 됨.
        /*if (null == idx || 0 == idx) {
            throw new MyloCommonException(ERROR_SAMPLE, ERROR_PARAM_VALIDITY, "invalid idx !!" + idx);
        }
        if (StringUtils.isEmpty(keyword)) {
            throw new MyloCommonException(ERROR_SAMPLE, ERROR_PARAM_VALIDITY, "invalid keyword  !!" + keyword);
        }*/

    }

}
