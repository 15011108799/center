package com.tlong.center.web;

import com.tlong.center.api.web.WebHtmlInfoApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/html")
public class WebHtmlInfoController implements WebHtmlInfoApi {

    /**
     * 获取左侧列表panel
     */
    @Override
    public String leftPanel(@RequestParam Integer var1) {
        return "1222222222222222222222222222222222222222222";
//        return "&lt;div class=&quot;panel panel-success&quot;&gt;&lt;div class=&quot;panel-heading&quot; role=&quot;tab&quot; id=&quot;headingSix&quot;&gt;&lt;h4 class=&quot;panel-title&quot;&gt;&lt;a class=&quot;collapsed&quot; role=&quot;button&quot; data-toggle=&quot;collapse&quot; data-parent=&quot;#accordion&quot; href=&quot;#collapseSix&quot; aria-expanded=&quot;false&quot; aria-controls=&quot;collapseSix&quot;&gt;订单管理&lt;/a&gt;&lt;/h4&gt;&lt;/div&gt;&lt;div id=&quot;collapseSix&quot; class=&quot;panel-collapse collapse bg-success&quot; role=&quot;tabpanel&quot;&gt;&lt;div class=&quot;panel-body&quot;&gt;&lt;a href=&quot;#&quot;&gt;订单管理&lt;/a&gt;&lt;/div&gt;&lt;/div&gt;&lt;/div&gt;";
    }
}
