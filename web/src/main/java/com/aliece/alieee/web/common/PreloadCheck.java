package com.aliece.alieee.web.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author by wuyuan
 */
@Controller
public class PreloadCheck {

    /**
     * 应用启动进行preload检查的路径.
     * 启动脚本会检查返回的文本中是否有success.
     * 注:preload文件并非健康检查文件.
     * @return
     */
    @RequestMapping("/status")
    @ResponseBody
    public String status(){
        return "SUCCESS";
    }
}
