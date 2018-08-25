package com.tlong.center.web.org;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.org.AddOrgRequestDto;
import com.tlong.center.api.dto.web.org.TlongOrgResponseDto;
import com.tlong.center.api.web.org.WebOrgApi;
import com.tlong.center.service.WebOrgService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/org")
public class WebOrgController implements WebOrgApi {

    private final WebOrgService webOrgService;

    public WebOrgController(WebOrgService webOrgService) {
        this.webOrgService = webOrgService;
    }

    /**
     * 新增机构
     * @param requestDto
     * @return
     */
    @Override
    public TlongResultDto addOrg(@RequestBody AddOrgRequestDto requestDto) {
        return webOrgService.addOrg(requestDto);
    }

    /**
     * 查询子级别代理商机构
     */
    @Override
    public Page<TlongOrgResponseDto> agentList(@PathVariable Long orgId, @RequestBody PageAndSortRequestDto pageAndSortRequestDto) {
        return webOrgService.agentList(orgId,pageAndSortRequestDto);
    }
}
