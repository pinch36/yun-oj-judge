package com.yun.oj.judge.service.impl;

import cn.hutool.json.JSONUtil;

import com.yun.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yun.oj.judge.manager.JudgeManager;
import com.yun.oj.judge.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yun.oj.common.common.ErrorCode;
import yun.oj.common.exception.BusinessException;
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
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Resource
    private RemoteCodeSandbox remoteCodeSandbox;
    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit judge(QuestionSubmit questionSubmit) {
        log.info("判题..");
        update(questionSubmit.getId(), 1, null,"更新提交状态为判题中失败");
        Question question = questionService.getById(questionSubmit.getQuestionId());
        try {
            List<JudgeCase> judgeCases = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
            List<String> input = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
            ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                    .input(input)
                    .code(questionSubmit.getCode())
                    .language(questionSubmit.getLanguage())
                    .build();
//            JudgeInfo judgeInfo = JudgeUtils.judge(executeCodeRequest);
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
            update(questionSubmit.getId(), 2,judgeInfo, "更新提交状态为判题成功失败");
            return questionSubmitService.getById(questionSubmit.getId());
        } catch (Exception e) {
            update(questionSubmit.getId(), 3,null, "判题异常");
            log.error("判题异常:", e);
            throw new RuntimeException(e);
        }
    }

    private void update(Long questionSubmitId, Integer status, JudgeInfo judgeInfo, String message) {
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(status);
        if (judgeInfo != null) {
            String judgeInfoJson = JSONUtil.toJsonStr(judgeInfo);
            questionSubmitUpdate.setJudgeInfo(judgeInfoJson);
        }
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            questionSubmitUpdate.setStatus(3);
            update = questionSubmitService.updateById(questionSubmitUpdate);
            if (!update) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新提交状态为判题失败失败");
            }
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, message);
        }
    }
}
