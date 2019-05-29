### aar+maven版本管理
系统：Centos6
环境：jdk8

下载jdk
```
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "https://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-linux-i586.rpm?AuthParam=1543200255_92b730036132b0ce3e9a7be91a489b28"
```
下载之后文件名不对，用rename修改后缀，然后`rpm -ivh jdk-87191-linux-i586.rpm `安装即可，使用java -version检查当前版本

配置Sonatype nexus环境：直接搭建或者用Docker搭建

#### 方式1
下载nexus，进入nexus下bin目录，然后./nexus start启动服务，第一次启动可以使用./nexus run命令可以查看nexus的运行状态（在内存较小的机器上会报Out of memory错误）
<br/>启动完毕后访问主页
http://localhost:8081/nexus/#welcome
<br/>初始用户名密码：`admin admin123`
#### 方式2
docker search nexus 直接拉取第一个star数量最多的
`docker pull sonatype/nexus3 `
<br/>运行nexus
`docker run -d -p 8081:8081 --name nexus sonatype/nexus3`
<br/>用logs查看nexus运行状态`docker logs -f nexus`

启动完毕后访问方式同方式1


### aar的引入方式
* 方式1：引用本地aar文件，将aar文件放在libs目录下面，app的gradle.build的android节点下添加 

 ``` 
 repositories {
     flatDir {
         dirs 'libs' 
      }
  }
 ```
缺点：如果多个模块都依赖这个aar文件，需要拷贝多次
* 方式2：建一个单独的module，gradle.build配置为
```
configurations.maybeCreate("default") 
artifacts.add("default", file('app-release.aar')) 
```
其它的模块依赖这个module即可

* 方式3：maven的方式
使用nexus搭建maven私有仓库，参考[Nexus 搭建私有maven仓库](https://blog.csdn.net/u011974987/article/details/52372185)
优点：有版本管理功能