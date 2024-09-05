package com.yun.oj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yun.oj.judge.codesandbox.CodeSandbox;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import yun.oj.common.common.ErrorCode;
import yun.oj.common.exception.BusinessException;
import yun.oj.model.model.dto.judge.ExecuteCodeRequest;
import yun.oj.model.model.dto.judge.ExecuteCodeResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/02/20:14
 * @Description:
 */
@Slf4j
@Component
public class RemoteCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        log.info("远程代码沙箱");
        String url = "http://localhost:8090/execute";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
