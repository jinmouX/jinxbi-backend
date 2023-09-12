package com.jinx.springbootinit.bizmq;

import cn.hutool.core.util.StrUtil;
import com.jinx.springbootinit.common.ErrorCode;
import com.jinx.springbootinit.constant.CommonConstant;
import com.jinx.springbootinit.constant.RabbitMqConstant;
import com.jinx.springbootinit.exception.BusinessException;
import com.jinx.springbootinit.manager.AiManager;
import com.jinx.springbootinit.model.entity.Chart;
import com.jinx.springbootinit.model.enums.ChartStatusEnum;
import com.jinx.springbootinit.service.ChartService;
import com.jinx.springbootinit.utils.ExcelUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class BiConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.BI_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = RabbitMqConstant.BI_EXCHANGE_NAME, durable = "true", type = RabbitMqConstant.BI_EXCHANGE_TYPE),
            key = RabbitMqConstant.BI_ROUTING_KEY
    ))
    public void consumer(String chartIdStr, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        log.info("bi 队列接收到消息，chartIdStr:{}",chartIdStr);
        if(StrUtil.isEmpty(chartIdStr)){
            channel.basicNack(tag,false,false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"图表ID不能为空");
        }
        long chartId = Long.parseLong(chartIdStr);
        Chart chart = chartService.getById(chartId);
        if(chart == null){
            channel.basicNack(tag,false,false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"图表信息未找到");
        }
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setStatus(ChartStatusEnum.Running.getStatus());
        boolean updateResult = chartService.updateById(updateChart);
        if(!updateResult){
            chartService.handleChartUpdateError(chartId,"图表运行状态更新失败");
            channel.basicNack(tag,false,true);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"图表运行状态更新失败");
        }
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = chart.getGoal();
        if (StringUtils.isNotBlank(chart.getChartType())) {
            userGoal += "，请使用" + chart.getChartType();
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String csvData = chart.getChartData();;
        userInput.append(csvData).append("\n");
        String result = aiManager.doChat(CommonConstant.BI_MODEL_ID, userInput.toString());
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            chartService.handleChartUpdateError(chart.getId(), "AI 处理异常");
            channel.basicNack(tag,false,true);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 处理异常");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        // 插入到数据库
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setStatus(ChartStatusEnum.SUCCESS.getStatus());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        boolean updateResult2 = chartService.updateById(updateChartResult);
        if(!updateResult2){
            chartService.handleChartUpdateError(chart.getId(),"图表成功状态更新失败");
            channel.basicNack(tag,false,true);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"图表成功状态更新失败");
        }
        channel.basicAck(tag,false);
    }
}
