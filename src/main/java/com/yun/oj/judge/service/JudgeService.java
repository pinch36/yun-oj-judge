package com.yun.oj.judge.service;


import yun.oj.model.model.entity.QuestionSubmit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/01/6:55
 * @Description:
 */
public interface JudgeService {
    QuestionSubmit judge(QuestionSubmit questionSubmit);
}
