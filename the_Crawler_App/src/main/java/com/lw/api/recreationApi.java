package com.lw.api;

import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName recreationApi
 * @Description TODO
 * @Author Li Wang
 * @Date 2021/7/1/001 17:51
 **/
@RestController
@RequestMapping("/recreation")
public class recreationApi {

    
    
    /**
     * @Description //TODO 获取所有城市
     **/
    @GetMapping("/getCity")
    public Object getCity() {
        String path = "http://jisuweather.api.bdymkt.com/weather/city";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.setCredentials("61625dac6c4548b88f154d5311e9bee8", "9e714c1febb54095a429fdd263d9c46d");
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description //TODO 根据城市名获取天气信息
     **/
    @PostMapping("/getWeather")
    public Object getWeather(String city) {
        String path = "http://gwgp-n6uzuwmjrou.n.bdcloudapi.com/weather/query";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.setCredentials("61625dac6c4548b88f154d5311e9bee8", "9e714c1febb54095a429fdd263d9c46d");
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("city", city);
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
}
