package com.jung9407.bookreviewapp.util;

import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PostValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GeneralForumEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        GeneralForumEntity generalForumEntity = (GeneralForumEntity) obj;
        if(StringUtils.isEmpty(generalForumEntity.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요.");
        }
    }
}
