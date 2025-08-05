package com.ctdi.cnos.llm.cache.aspect;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpressionEvaluator extends CachedExpressionEvaluator {
	private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
	private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);
	private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);
	private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

	public EvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method,
			Object[] args) {
		Method targetMethod = getTargetMethod(targetClass, method);
		ExpressionRootObject root = new ExpressionRootObject(method, args, object, targetClass);
		return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
	}

	public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
	}

	public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression)
				.getValue(evalContext, Boolean.class)));
	}

	private Method getTargetMethod(Class<?> targetClass, Method method) {
		AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
		Method targetMethod = this.targetMethodCache.get(methodKey);
		if (targetMethod == null) {
			targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
			if (targetMethod == null) {
				targetMethod = method;
			}
			this.targetMethodCache.put(methodKey, targetMethod);
		}
		return targetMethod;
	}
}
