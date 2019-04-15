package cn.smallmartial.exception;

import cn.smallmartial.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author smallmartial
 * @Date 2019/4/11
 * @Email smallmarital@qq.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

/*    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }*/

}
