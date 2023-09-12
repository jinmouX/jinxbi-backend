package com.jinx.springbootinit.service;

import com.jinx.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author jinx
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-08-28 17:37:47
*/
public interface ChartService extends IService<Chart> {
    void handleChartUpdateError(long chartId,String execMessage);

}
