package com.yun.oj.judge.manager;

import com.yun.oj.judge.strategy.DefaultJudgeStrategy;
import com.yun.oj.judge.strategy.JavaJudgeStrategy;
import com.yun.oj.judge.strategy.JudgeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yun.oj.model.model.dto.judge.JudgeContext;
import yun.oj.model.model.dto.judge.JudgeInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/03/19:39
 * @Description:
 */
@Component
@Slf4j
public class JudgeManager {
     public JudgeInfo judge(JudgeContext judgeContext){
         String language = judgeContext.getQuestionSubmit().getLanguage();
         JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
         switch (language){
             case "java":
                 judgeStrategy = new JavaJudgeStrategy();
                 break;
         }
         return judgeStrategy.judge(judgeContext);
     }
}
