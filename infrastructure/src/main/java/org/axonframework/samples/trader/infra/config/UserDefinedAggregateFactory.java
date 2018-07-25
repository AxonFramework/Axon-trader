package org.axonframework.samples.trader.infra.config;

import org.axonframework.eventsourcing.AbstractAggregateFactory;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDefinedAggregateFactory<T> implements AggregateFactory<T>, InitializingBean,
        ApplicationContextAware, BeanNameAware {

    private String beanName;
    private ApplicationContext applicationContext;
    private Class<T> aggregateType;
    private AggregateFactory<T> delegate;

    @Override
    public T createAggregateRoot(String aggregateIdentifier, DomainEventMessage<?> firstEvent) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getAggregateType() {
        if (aggregateType == null) {
            aggregateType = (Class<T>) applicationContext.getType(beanName);
        }
        return aggregateType;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        this.delegate = new AbstractAggregateFactory<T>(getAggregateType()) {
            @Override
            protected T doCreateAggregate(String aggregateIdentifier, DomainEventMessage firstEvent) {
                return (T) applicationContext.getBean(beanName);
            }

            @Override
            protected T postProcessInstance(T aggregate) {
                applicationContext.getAutowireCapableBeanFactory().configureBean(aggregate, beanName);
                return aggregate;
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
