package com.yun.oj.judge.codesandbox;

import yun.oj.model.model.dto.judge.ExecuteCodeRequest;
import yun.oj.model.model.dto.judge.ExecuteCodeResponse;

/**
 * Created with IntelliJ IDEA.
 * @Author: __yun
 * @Date: 2024/09/02/20:13
 * @Description: 
 */public interface CodeSandbox {
     ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest);
}
