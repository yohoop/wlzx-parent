package net.wanho.manage_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    //交换器的名称
    @Value("${wlzx.mq.exchange}")
    public  String exchange_name;

    //队列的名称
    @Value("${wlzx.mq.queue}")
    public  String queue_name;

    //路由键 即站点Id
    @Value("${wlzx.mq.routingKey}")
    public  String routingKey;

    // 创建direct类型交换器
    @Bean
    public Exchange getExchange() {
        return ExchangeBuilder.directExchange(exchange_name).durable(true).build();
    }
    //创建队列
    @Bean
    public Queue getQueue() {
        Queue queue = new Queue(queue_name);
        return queue;
    }
    //绑定队列到交换器
    @Bean
    public Binding getBinding(@Autowired Queue queue, @Autowired Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
