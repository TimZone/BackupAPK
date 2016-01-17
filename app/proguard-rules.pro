#指定代码的压缩级别
-optimizationpasses 5 
#包明不混合大小写
-dontusemixedcaseclassnames 
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses 
#优化 不优化输入的类文件
-dontoptimize 
#预校验
-dontpreverify 
#混淆时是否记录日志
-verbose
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/,!class/merging/ 
#保护注解
-keepattributes Annotation 
#保持哪些类不被混淆
-keep public class * extends android.app.Fragment 
-keep public class * extends android.app.Activity 
-keep public class * extends android.app.Application 
-keep public class * extends android.app.Service 
-keep public class * extends android.content.BroadcastReceiver 
-keep public class * extends android.content.ContentProvider 
-keep public class * extends android.app.backup.BackupAgentHelper 
-keep public class * extends android.preference.Preference 
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment 
#忽略警告
-ignorewarning 
#记录生成的日志数据,gradle build时在本项目根目录输出
#apk 包内所有 class 的内部结构
-dump class_files.txt 
#未混淆的类和成员
-printseeds seeds.txt 
#列出从 apk 中删除的代码
-printusage unused.txt 
#混淆前后的映射
-printmapping mapping.txt