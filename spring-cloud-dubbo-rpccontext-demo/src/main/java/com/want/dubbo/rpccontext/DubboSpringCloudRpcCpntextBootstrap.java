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

package com.want.dubbo.rpccontext;


import com.want.dubbo.api.service.StoreService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * Dubbo Spring Cloud Consumer Bootstrap.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */

@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableCaching
@EnableOpenApi
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class DubboSpringCloudRpcCpntextBootstrap {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DubboSpringCloudRpcCpntextBootstrap.class)
				.properties("spring.profiles.active=nacos").run(args);
	}

	@DubboReference(version = "2.0.0")
	private StoreService storeService;

	@Bean
	public ApplicationRunner applicationRunner(){
		return args -> storeService.list();
	}

}
