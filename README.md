# AIDLTest

效果图：



其实很简单，服务端有一个“败家之眼”的图片，我们要做的是：远程到服务端，然后获得服务端的图片，显示到自己的 ImageView 上边。


1、创建远程端，指定远程端的进程，确保我们的两个应用处于同一进程
让我们应用处于同一进程很简单，只需要在清单文件中设置android:process属性就可以了
但是对于 process 的属性值的命名，是有一些区别的：

   * 默认，不填写，进程名称为当前项目的包名
   * 完整路径，比如 “com.example.lixue.aidltest” 就是完整路径；
     以完整路径为名的应用可以让其他应用通过 shareUID 的方式使其共享同一进程资源    
   * “:”开头，比如“:remote”这种方式，那么进程名称则为“包名”+“:remote”，
      比如“com.example.lixue.aidltest:remote”；
      用这种方式命名的进程名，属于私有进行，其他进程不能与其共享进程资源
那么，我们这个例子是用来演示 “客户端进程” 和 “服务端进程” 相互通信的，那么最合适的就是使用第三种声明方式。


2、第二步，我们需要在服务端创建并改写 AIDL 文件，Android Studio 已经为我们提供了非常方便的方式去创建 AIDL 文件，
全程操作十分简洁，只需要在服务端 module 的任何位置，点击右键，选择创建 AIDL 文件即可
创建好了之后，我们接下来要对这个 AIDL 文件进行修改了，AIDL 默认给我们提供的这个方法basicTypes()一般不需要(大可直接删掉即可)，
我们只需要在这个IMyAidlInterface接口中添加我们想做的操作就可以了，比如我们这里就是获取 remote 端的一个图片，这个图片我们放到 remote 端的 assets 目录下，通过这个方法可以得到这个图片的 Bitmap ，然后显示到客户端的图片上。
所以我们在 Aidl 文件内部添加一个getImage()方法，返回值则为 Bitmap

3、上面两步，我们基本上没做什么事情，也就是写了一两行代码，写完之后，我们需要重新编译一下项目，
得到 Android Studio 为我们自动生成的IMyAidlInterface这个接口对应的实现：Stub文件类
简而言之，就是经过第二步我们对 Aidl 文件的创建和修改之后，这个时候，我们编译代码，代码就会为我们生成真正的Aidl.java文件，
这个文件才是实现进程间通信的关键。等于说 为了避免我们写复杂的代码实现进程间通信，
google 特意在 Android Studio 中添加了创建 Aidl 的捷径，真是太 nice 了。

4、接下来的工作，就是要在服务端为客户端提供远程服务service,以便于让客户端获取服务端的数据，或者调用服务端的方法。
如有疑惑，看下注释

5、经过前面4小步，服务端就已经弄好了，接下来，只需要让客户端远程调用一下就可以了
此时，我们还得需要将 remote 服务端创建的 aidl 文件复制到我们的客户端来
然后再编译一下。编译的目的是让客户端也生成对应的adil.java文件

6、编译完成之后，就可以安心的写远程服务端的代码了。
我们知道，我们可以通过开启或绑定的方式去访问另一个进程的应用，这里我们使用绑定的方式进行对 remote 服务端的访问。
由于是远程另一个应用，这里的bindService(intent)中的 intent 肯定就不能用显式的了，而是需要用隐式的，
使用隐式跳转首先要保证你的 remote 服务端的清单文件对 service 有类似这样的配置
然后准备好绑定 service 的 ServiceConnection,注意，这个部分需要在复制完 aidl 文件夹，然后构建之后才不会报错
然后在客户端的布局中，顶部准备了三个按钮，用来绑定及操作，中心一个 ImageView 用于显示从服务端获取的 Bitmap。

注：之所以使用子线程，是考虑到服务端的读取图片并转化成 Bitmap 可能会稍稍耗时，所以将远程服务端至于子线程
android 5.0 之后，必须按照红框中的方式进行「隐式」调用：即需要指定 service 的 action 和 package ，android 5.0 之前，可以只使用 action

7、验收成果啦
先将 remote 服务端安装到设备上，然后再安装客户端，就可以看到顶部的效果了~~