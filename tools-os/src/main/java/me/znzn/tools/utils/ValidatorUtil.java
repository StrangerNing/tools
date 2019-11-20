package me.znzn.tools.utils;

import com.google.common.base.Joiner;
import me.znzn.tools.common.exception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

/**
 * 校验工具类
 *
 */
public class ValidatorUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * hibernate validator校验
     *
     * @param obj 校验对象
     * @return void
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        List<String> list = new ArrayList<>();
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation constraintViolation : constraintViolations) {
                list.add(constraintViolation.getMessage());
            }
        }
        if (list != null && list.size() > 0) {
            throw new BusinessException(Joiner.on(",").skipNulls().join(list));
        }
    }

    /**
     * 校验bean返回map形式的校验结果
     *
     * @param obj bean
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static <T> Map<String, String> validateToMap(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        Map<String, String> map = new HashMap<>(constraintViolations.size());
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation constraintViolation : constraintViolations) {
                map.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        return map;
    }

    /**
     * 校验bean返回map形式的校验结果
     *
     * @param obj bean
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static <T> List<String> validateToList(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        List<String> list = new ArrayList<>();
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation constraintViolation : constraintViolations) {
                list.add(constraintViolation.getMessage());
            }
        }
        return list;
    }
}
