package com.jinx.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinx.springbootinit.model.entity.Chart;
import com.jinx.springbootinit.model.enums.ChartStatusEnum;
import com.jinx.springbootinit.service.ChartService;
import com.jinx.springbootinit.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author jinx
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2023-08-28 17:37:47
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{
    @Override
    public void handleChartUpdateError(long chartId,String execMessage){
        Chart chart = new Chart();
        chart.setId(chartId);
        chart.setStatus(ChartStatusEnum.FAIL.getStatus());
        chart.setExecMessage(execMessage);
        updateById(chart);
    }

}




