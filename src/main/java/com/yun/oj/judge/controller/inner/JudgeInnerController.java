package com.yun.oj.judge.controller.inner;

import com.yun.oj.judge.service.JudgeService;
import com.yun.oj.service.client.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yun.oj.model.model.entity.QuestionSubmit;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/06/8:59
 * @Description:
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/do")
    public QuestionSubmit judge(@RequestBody QuestionSubmit questionSubmit) {
        return judgeService.judge(questionSubmit);
    }
}
