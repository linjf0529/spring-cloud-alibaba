spring-cloud-starter-alibaba-nacos-config 2021.0.1.0模块移除了 spring-cloud-starter-bootstrap 依赖,  
如果你想以旧版的方式使用，你需要手动加上该依赖，现在推荐使用 spring.config.import 方式引入配置.  
spring:  
&nbsp;&nbsp;config:  
&nbsp;&nbsp;&nbsp;import:  
&nbsp;&nbsp;&nbsp;&nbsp;- optional:nacos:test.yml  # 监听 DEFAULT_GROUP:test.yml  
&nbsp;&nbsp;&nbsp;&nbsp;- optional:nacos:test01.yml?group=group_01 # 覆盖默认 group，监听 group_01:test01.yml  
&nbsp;&nbsp;&nbsp;&nbsp;- optional:nacos:test02.yml?group=group_02&refreshEnabled=false # 不开启动态刷新  
&nbsp;&nbsp;&nbsp;&nbsp;- nacos:test03.yml # 在拉取nacos配置异常时会快速失败，会导致 spring 容器启动失败  

下方配置文件会覆盖上方配置文件 

参考:[SpringCloud Alibaba 2021版 nacos 配置中心教程](https://huaweicloud.csdn.net/6385ad49fb4f2a0789d9b23d.html?spm=1001.2101.3001.6650.15&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Eactivity-15-123719707-blog-122846351.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Eactivity-15-123719707-blog-122846351.pc_relevant_aa&utm_relevant_index=16)
