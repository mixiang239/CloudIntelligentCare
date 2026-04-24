云端智能看护 Android 应用
========================

项目简介
--------
本应用是一款面向老年人群体的智慧养老看护应用，具备远程健康监测、紧急呼救、实时警报等功能，通过与服务器建立 Socket 长连接，实现数据的实时传输与状态同步。


目录结构
--------
app/                    主应用模块（MainActivity、导航栏）
core/
  common/              公共组件（路由路径、BaseApplication、IHostActivity 接口）
  data/                数据模型（Contact、Record、SettingCard 等）
  network/             网络功能（ServerConnection、AlarmPlayer、网络对话框）
feature/
  home/                首页（状态卡片、心率/步数、紧急呼救按钮）
  emergency/           紧急呼救（联系人列表、呼叫 120、警报测试）
  health/              健康监测（服药提醒、周步数图表）
  record/              记录历史（健康检测记录、警告事件）
  alarm/               警报页面（全屏警报音 + 震动）
  setting/             设置页面（账户、监护、其他）


核心功能说明
------------

1. 首页 (HomeFragment)
   - 监护状态展示（服务器连接指示灯呼吸动画）
   - 心率、步数快捷卡片，点击跳转健康页
   - 一键呼救按钮：单击跳转紧急页，长按 3 秒触发紧急呼叫
   - 电池、定位快捷卡片，点击打开对应系统设置
   - 最近活动列表，依次滑入动画
   - 卡片按压/释放缩放动画 + 涟漪反馈

2. 网络连接 (ServerConnection)
   - 基于 Socket 的 TCP 长连接，端口默认 8888
   - 自动心跳保活
   - JSON 消息格式，支持紧急状态推送
   - 连接状态实时更新到首页指示灯

3. 警报系统 (AlarmPlayer + AlarmFragment)
   - 收到紧急状态 → 自动跳转到 AlarmFragment（全屏红色页面）
   - 播放 raw/warning.mp3，同时调用 Vibrator 震动
   - 播放时自动调大音量到最大，停止后恢复原音量
   - 点击"停止警报"按钮返回首页，不退出应用

4. 紧急呼救 (EmergencyFragment)
   - 紧急联系人列表（Contact 数据模型）
   - 一键呼叫 120
   - 警报测试按钮（触发/停止警报）
   - 平安报告功能

5. 健康监测 (HealthFragment)
   - 服药提醒卡片
   - 周步数图表展示
   - 体检预约入口

6. 记录历史 (RecordFragment)
   - 健康检测记录列表
   - 警告事件记录列表
   - 按时间筛选功能

7. 设置页面 (SettingsFragment)
   - 账户设置（头像、姓名）
   - 监护设置（跌倒检测灵敏度等）
   - 其他设置（设备连接、通知）


关键技术
--------
ARouter           组件化路由，实现页面跳转和模块解耦
ViewBinding       视图绑定
Material Design  MaterialCardView、MaterialAlertDialog
Socket (TCP)      与服务器长连接
MediaPlayer       播放警报音频
Vibrator          设备震动
RecyclerView      列表展示（双层嵌套适配器）
BottomNavigationView  底部导航栏


页面路由
--------
/app/main         MainActivity 入口
/home/main        HomeFragment 首页
/emergency/main   EmergencyFragment 紧急呼救
/health/main      HealthFragment 健康监测
/record/main      RecordFragment 记录历史
/alarm/page       AlarmFragment 警报页面
/setting/main     SettingsFragment 设置页


主要接口
-------
IHostActivity (core/common)
  - navigateTo(path)           跳转到指定页面
  - getHostActivity()         获取宿主 Activity
  - startAlarm(sequence)       启动警报
  - stopAlarm()                停止警报
  - isAlarmPlaying()           警报是否正在播放
  - setAlarmCallback(callback) 设置警报回调

NetworkCallback (core/network)
  - onConnected(ip, port)      连接成功
  - onConnectionFailed(error)  连接失败
  - onMessageReceived(msg)     收到消息
  - onDisconnected()           连接断开
  - onEmergencyStatus(status, seq) 紧急状态变化


编译说明
--------
本项目采用 Gradle 多模块构建，确保各 feature 模块与 core 模块版本一致。
修改代码后执行 Build / Rebuild Project 即可。
