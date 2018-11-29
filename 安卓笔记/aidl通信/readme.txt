https://www.cnblogs.com/chase1/p/7135961.html

useage
总结

这是一个在AS下最简单的一个AIDL编程： 
1.服务端创建一个aidl目录，然后在该目录下新建一个.aidl为后缀的接口类，该类定义远程调用的接口方法。 
2.build编译之后会在app/build/generated/source/aidl/debug目录下会生成aidl远程实现类，该类是AS自动生成的。 
3.在AndroidManifest.xml下配置Service的action和process属性。 
4.将服务端的aidl目录拷贝到客户端相应的目录下，然后编写客户端调用代码，AS下简单的aidl编程就ok了。