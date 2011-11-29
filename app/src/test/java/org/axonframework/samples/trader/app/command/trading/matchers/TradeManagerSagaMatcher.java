/*
 * Copyright (c) 2011. Gridshore
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.app.command.trading.matchers;

import org.axonframework.samples.trader.app.api.portfolio.item.ReserveItemsCommand;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author Jettro Coenradie
 */
public abstract class TradeManagerSagaMatcher<T> extends ArgumentMatcher<T> {
    protected String problem;

    @Override
    public boolean matches(Object argument) {
        Object singleArgument;
        if (argument instanceof List) {
            singleArgument = ((List) argument).get(0);
        } else {
            singleArgument = argument;
        }
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<?> clazz = (Class<?>) pt.getActualTypeArguments()[0];
        if (!argument.getClass().equals(clazz)) {
            problem = String.format("Wrong argument type, required %s but received %s", ReserveItemsCommand.class.getName(), argument.getClass().getName());
        }
        return doMatch((T) singleArgument);
    }

    public abstract boolean doMatch(T singleArgument);

    @Override
    public void describeTo(Description description) {
        description.appendText(problem);
    }

}
