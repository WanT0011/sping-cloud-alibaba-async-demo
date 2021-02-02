/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.want.dubbo.provider.service;

import com.want.dubbo.api.domain.entity.User;
import com.want.dubbo.api.service.UserService;
import com.want.dubbo.provider.util.SleepUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * In-Memory {@link UserService} implementation.
 */
@DubboService(version = "1.0.0",protocol = "dubbo")
public class InMemoryUserServiceImpl implements UserService, SmartInitializingSingleton {

	private Map<Integer, User> usersRepository;


	/**
	 * 获取用户的信息
	 *
	 * @param uid
	 * @return
	 */
	@Override
	public User info(Integer uid) {
		SleepUtil.sleepLessOneSecond();
		return usersRepository.get(uid);
	}

	/**
	 * 初始化 10个用户
	 */
	@Override
	public void afterSingletonsInstantiated() {
		usersRepository = Stream.iterate(1,n -> n+1)
				.limit(10)
				.map(User::new)
				.collect(Collectors.toMap(User::getId, Function.identity(),(v1,v2) -> v1,() -> new ConcurrentHashMap<>(16)));
	}
}
