package com.yun.oj.judge.strategy;


import yun.oj.model.model.dto.judge.JudgeContext;
import yun.oj.model.model.dto.judge.JudgeInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/03/19:16
 * @Description:
 */
public interface JudgeStrategy {
    JudgeInfo judge(JudgeContext judgeContext);
}
