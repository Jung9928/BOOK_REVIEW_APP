package com.jung9407.bookreviewapp.util;

import com.jung9407.bookreviewapp.model.entity.PostEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PostValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PostEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        PostEntity postEntity = (PostEntity) obj;
        if(StringUtils.isEmpty(postEntity.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요.");
        }
    }
}
