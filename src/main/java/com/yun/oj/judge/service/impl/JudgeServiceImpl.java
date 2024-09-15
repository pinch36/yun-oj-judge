package com.yun.oj.judge.service.impl;

import cn.hutool.json.JSONUtil;

import com.yun.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yun.oj.judge.manager.JudgeManager;
import com.yun.oj.judge.service.JudgeService;
import com.yun.oj.service.client.service.QuestionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import yun.oj.model.model.dto.judge.ExecuteCodeRequest;
import yun.oj.model.model.dto.judge.ExecuteCodeResponse;
import yun.oj.model.model.dto.judge.JudgeContext;
import yun.oj.model.model.dto.judge.JudgeInfo;
import yun.oj.model.model.dto.question.JudgeCase;
import yun.oj.model.model.entity.Question;
import yun.oj.model.model.entity.QuestionSubmit;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/01/6:56
 * @Description:
 */
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {
    @Resource
    @Lazy
    private QuestionFeignClient questionFeignClient;
    @Resource
    private RemoteCodeSandbox remoteCodeSandbox;
    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit judge(QuestionSubmit questionSubmit) {
        log.info("判题..");
        Question question = questionFeignClient.getQuestionById(questionSubmit.getQuestionId());
        try {
            List<JudgeCase> judgeCases = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
            List<String> input = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
            ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                    .input(input)
                    .code(questionSubmit.getCode())
                    .language(questionSubmit.getLanguage())
                    .build();
            ExecuteCodeResponse executeCodeResponse = remoteCodeSandbox.execute(executeCodeRequest);
            JudgeContext judgeContext = JudgeContext.builder()
                    .judgeCaseList(judgeCases)
                    .input(input)
                    .output(executeCodeResponse.getOutput())
                    .judgeInfo(executeCodeResponse.getJudgeInfo())
                    .questionSubmit(questionSubmit)
                    .question(question)
                    .build();
            JudgeInfo judgeInfo = judgeManager.judge(judgeContext);
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
            return questionSubmit;
        } catch (Exception e) {
            log.error("判题异常:", e);
            throw new RuntimeException(e);
        }
    }
}
