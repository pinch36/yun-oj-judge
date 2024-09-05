package com.yun.oj.judge.strategy;


import yun.oj.model.model.dto.judge.JudgeContext;
import yun.oj.model.model.dto.judge.JudgeInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/03/19:42
 * @Description:
 */
public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo judge(JudgeContext judgeContext) {
        return new JavaJudgeStrategy().judge(judgeContext);
    }
}
