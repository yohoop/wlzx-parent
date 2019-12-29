package net.wanho.manage_media.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
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

    //路由键
    @Value("${wlzx.mq.routingkey}")
    public  String routingKey;


    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;

    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /**
     * 交换机配置
     * @return the exchange
     */
    @Bean
    public Exchange getExchange() {
        return ExchangeBuilder.directExchange(exchange_name).durable(true).build();
    }
    //声明队列
    @Bean
    public Queue getQueue() {
        Queue queue = new Queue(queue_name,true,false,true);
        return queue;
    }
    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_media_processtask(@Autowired Queue queue, @Autowired Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
