package com.yun.oj.judge.strategy;

import cn.hutool.json.JSONUtil;
import yun.oj.model.model.dto.judge.JudgeContext;
import yun.oj.model.model.dto.judge.JudgeInfo;
import yun.oj.model.model.dto.question.JudgeCase;
import yun.oj.model.model.dto.question.JudgeConfig;
import yun.oj.model.model.entity.Question;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/03/19:19
 * @Description:
 */
public class JavaJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo judge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTimeout()).orElse(0L);
        List<String> input = judgeContext.getInput();
        List<String> output = judgeContext.getOutput();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTimeout(time);
        if (input.size() != output.size()) {
            judgeInfoResponse.setResult("答案错误");
            return judgeInfoResponse;
        }
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!output.get(i).equals(judgeCase.getOutput())) {
                judgeInfoResponse.setResult("答案错误");
                return judgeInfoResponse;
            }
        }
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory > memoryLimit) {
            judgeInfoResponse.setResult("内存溢出");
            return judgeInfoResponse;
        }
        // Java 程序本身需要额外执行 10 秒钟
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if ((time - JAVA_PROGRAM_TIME_COST) > timeLimit) {
            judgeInfoResponse.setResult("超时");
            return judgeInfoResponse;
        }
        judgeInfoResponse.setResult("AC");
        return judgeInfoResponse;
    }
}
