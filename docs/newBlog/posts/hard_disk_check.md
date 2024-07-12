---
title: 给一块新硬盘举行的欢迎仪式
authors:
    - shafish
date:
    created: 2023-03-12
    updated: 2024-02-02
categories:
    - hhd
---

很久之前写的，刚好最近又用到 [https://blog.shafish.cn/archives/1670/](https://blog.shafish.cn/archives/1670/){target=_blank}

> 安装好然后虔诚地按下电源开关。

## 一、定位路径
```shell
# 根据容量定位 /dev/sda
sudo fdisk -l
```
## 二、进入检测仪式
### 1.查看硬盘基本信息、检查是否支持SMART测试
`sudo smartctl -i /dev/sda`

```
=== START OF INFORMATION SECTION ===
Model Family:     Western Digital Red
Device Model:     WDC WD40EFRX-68NxxNx
Serial Number:    WD-WCC7K4FXPxxR
LU WWN Device Id: 5 0014ee 211f4dd29
Firmware Version: 80.00A80
User Capacity:    4,000,787,030,016 bytes [4.00 TB]
Sector Sizes:     512 bytes logical, 4096 bytes physical
Rotation Rate:    5400 rpm
Form Factor:      3.5 inches
Device is:        In smartctl database [for details use: -P show]
ATA Version is:   ACS-3 T13/2161-D revision 5
SATA Version is:  SATA 3.1, 6.0 Gb/s (current: 6.0 Gb/s)
Local Time is:    Thu Jan 14 22:45:01 2021 CST
SMART support is: Available - device has SMART capability.
SMART support is: Enabled
```
看最后两项满足要求就可以进行各种SMART测试咧。

<!-- more -->

### 2.查看硬盘的详细信息
`sudo smartctl -A /dev/sda`

```
=== START OF READ SMART DATA SECTION ===
SMART Attributes Data Structure revision number: 16
Vendor Specific SMART Attributes with Thresholds:
ID# ATTRIBUTE_NAME          FLAG     VALUE WORST THRESH TYPE      UPDATED  WHEN_FAILED RAW_VALUE
  1 Raw_Read_Error_Rate     0x002f   100   253   051    Pre-fail  Always       -       0
  3 Spin_Up_Time            0x0027   100   253   021    Pre-fail  Always       -       0
  4 Start_Stop_Count        0x0032   100   100   000    Old_age   Always       -       2
  5 Reallocated_Sector_Ct   0x0033   200   200   140    Pre-fail  Always       -       0
  7 Seek_Error_Rate         0x002e   100   253   000    Old_age   Always       -       0
  9 Power_On_Hours          0x0032   100   100   000    Old_age   Always       -       0
 10 Spin_Retry_Count        0x0032   100   253   000    Old_age   Always       -       0
 11 Calibration_Retry_Count 0x0032   100   253   000    Old_age   Always       -       0
 12 Power_Cycle_Count       0x0032   100   100   000    Old_age   Always       -       2
192 Power-Off_Retract_Count 0x0032   200   200   000    Old_age   Always       -       1
193 Load_Cycle_Count        0x0032   200   200   000    Old_age   Always       -       1
194 Temperature_Celsius     0x0022   125   125   000    Old_age   Always       -       25
196 Reallocated_Event_Count 0x0032   200   200   000    Old_age   Always       -       0
197 Current_Pending_Sector  0x0032   200   200   000    Old_age   Always       -       0
198 Offline_Uncorrectable   0x0030   100   253   000    Old_age   Offline      -       0
199 UDMA_CRC_Error_Count    0x0032   200   253   000    Old_age   Always       -       0
200 Multi_Zone_Error_Rate   0x0008   100   253   000    Old_age   Offline      -       0
```

- 01（001） Raw_Read_Error_Rate 底层数据读取错误率 
- 04（004） Start_Stop_Count 启动/停止计数 
- 05（005） Reallocated_Sector_Ct 重映射扇区数 
- 09（009） Power_On_Hours 通电时间累计，出厂后通电的总时间，一般磁盘寿命三万小时 
- 0A（010） Spin_Retry_Count 主轴起旋重试次数（即硬盘主轴电机启动重试次数） 
- 0B（011） Calibration_Retry_Count 磁盘校准重试次数 
- 0C（012） Power_Cycle_Count 磁盘通电次数 
- C2（194） Temperature_Celsius 温度 
- C7（199） UDMA_CRC_Error_Count 奇偶校验错误率 
- C8（200） Write_Error_Rate: 写错误率 
- F1（241） Total_LBAs_Written：表示磁盘自出厂总共写入的的数据，单位是LBAS=512Byte 
- F2（242） Total_LBAs_Read：表示磁盘自出厂总共读取的数据，单位是LBAS=512Byte

### 3.来一次硬盘长检
`sudo smartctl -t long /dev/sda` 

```shell
# 等了462分钟后
```
### 4.查看检测结果
`sudo smartctl --all /dev/sda` 显示全部检测信息
`sudo smartctl -l selftest  /dev/sda` 显示硬盘检测日志
`sudo smartctl -l error   /dev/sda` 显示硬盘错误汇总
```shell
=== START OF INFORMATION SECTION ===
Model Family:     Western Digital Red
Device Model:     WDC WD40EFRX-68NxxNx
Serial Number:    WD-WCC7K4FXPxxR
LU WWN Device Id: 5 0014ee 211f4dd29
Firmware Version: 80.00A80
User Capacity:    4,000,787,030,016 bytes [4.00 TB]
Sector Sizes:     512 bytes logical, 4096 bytes physical
Rotation Rate:    5400 rpm  # 不是买不起7200，要的是低功耗好不好
Form Factor:      3.5 inches
Device is:        In smartctl database [for details use: -P show]
ATA Version is:   ACS-3 T13/2161-D revision 5
SATA Version is:  SATA 3.1, 6.0 Gb/s (current: 6.0 Gb/s)
Local Time is:    Fri Jan 15 20:54:11 2021 CST
SMART support is: Available - device has SMART capability.
SMART support is: Enabled

=== START OF READ SMART DATA SECTION ===
SMART overall-health self-assessment test result: PASSED

General SMART Values:
Offline data collection status:  (0x80) Offline data collection activity
                                        was never started.
                                        Auto Offline Data Collection: Enabled.
Self-test execution status:      (   0) The previous self-test routine completed
                                        without error or no self-test has ever 
                                        been run.
Total time to complete Offline 
data collection:                (43440) seconds.
Offline data collection
capabilities:                    (0x7b) SMART execute Offline immediate.
                                        Auto Offline data collection on/off support.
                                        Suspend Offline collection upon new
                                        command.
                                        Offline surface scan supported.
                                        Self-test supported.
                                        Conveyance Self-test supported.
                                        Selective Self-test supported.
SMART capabilities:            (0x0003) Saves SMART data before entering
                                        power-saving mode.
                                        Supports SMART auto save timer.
Error logging capability:        (0x01) Error logging supported.
                                        General Purpose Logging supported.
Short self-test routine 
recommended polling time:        (   2) minutes.
Extended self-test routine
recommended polling time:        ( 462) minutes.
Conveyance self-test routine
recommended polling time:        (   5) minutes.
SCT capabilities:              (0x3035) SCT Status supported.
                                        SCT Feature Control supported.
                                        SCT Data Table supported.

SMART Attributes Data Structure revision number: 16
Vendor Specific SMART Attributes with Thresholds:
ID# ATTRIBUTE_NAME          FLAG     VALUE WORST THRESH TYPE      UPDATED  WHEN_FAILED RAW_VALUE
  1 Raw_Read_Error_Rate     0x002f   100   253   051    Pre-fail  Always       -       0
  3 Spin_Up_Time            0x0027   100   253   021    Pre-fail  Always       -       0
  4 Start_Stop_Count        0x0032   100   100   000    Old_age   Always       -       3
  5 Reallocated_Sector_Ct   0x0033   200   200   140    Pre-fail  Always       -       0
  7 Seek_Error_Rate         0x002e   100   253   000    Old_age   Always       -       0
  9 Power_On_Hours          0x0032   100   100   000    Old_age   Always       -       0
 10 Spin_Retry_Count        0x0032   100   253   000    Old_age   Always       -       0
 11 Calibration_Retry_Count 0x0032   100   253   000    Old_age   Always       -       0
 12 Power_Cycle_Count       0x0032   100   100   000    Old_age   Always       -       3
192 Power-Off_Retract_Count 0x0032   200   200   000    Old_age   Always       -       1
193 Load_Cycle_Count        0x0032   200   200   000    Old_age   Always       -       5
194 Temperature_Celsius     0x0022   130   122   000    Old_age   Always       -       20
196 Reallocated_Event_Count 0x0032   200   200   000    Old_age   Always       -       0
197 Current_Pending_Sector  0x0032   200   200   000    Old_age   Always       -       0
198 Offline_Uncorrectable   0x0030   100   253   000    Old_age   Offline      -       0
199 UDMA_CRC_Error_Count    0x0032   200   253   000    Old_age   Always       -       0
200 Multi_Zone_Error_Rate   0x0008   100   253   000    Old_age   Offline      -       0

SMART Error Log Version: 1
No Errors Logged

SMART Self-test log structure revision number 1
Num  Test_Description    Status                  Remaining  LifeTime(hours)  LBA_of_first_error
# 1  Short offline       Completed without error       00%         0         -

SMART Selective self-test log data structure revision number 1
 SPAN  MIN_LBA  MAX_LBA  CURRENT_TEST_STATUS
    1        0        0  Not_testing
    2        0        0  Not_testing
    3        0        0  Not_testing
    4        0        0  Not_testing
    5        0        0  Not_testing
Selective self-test flags (0x0):
  After scanning selected spans, do NOT read-scan remainder of disk.
If Selective self-test is pending on power-up, resume after 0 minute delay
```
- 木有错误报告
- 197 Current_Pending_Sector的RAW_VALUE为0说明没有坏道

## 三、检测汇总以及检测理由
### 1.需留意的硬盘信息
- 品牌：Model Family
- 容量：User Capacity
- 通电时间：9 Power_On_Hours
- 通电次数：4 Start_Stop_Count
- 是否存在坏道：197 Current_Pending_Sector

### 2.检测建议及理由
- 可以2个月搞一次长检`sudo smartctl -t long /dev/sda`，没必要太频繁。
- 查看通电时间、次数判断是否是新硬盘
- 检测出硬盘坏道多时，必须进行数据转移及备份
- 坏道修复`badblocks /dev/sda`

## 四、格式化
不考虑分区（GParted），不考虑window兼容（NTFS），直接ext4格式化，买来就是给linux用的，爽。
`sudo mkfs.ext4 /dev/sda`

如果是 `raid`盘，会提示 `/dev/sdx is apparently in use by the system; will not make a filesystem here!`，此时只需使用 mdadm 来停止作业，重新格式化即可。

``` shell title="lsblk -l /dev/sdx"
sde           8:64   0  12.7T  0 disk
├─md126       9:126  0     0B  0 raid5
└─md127       9:127  0     0B  0 md
```

``` shell
sudo mdadm --stop /dev/md126
sudo mdadm --stop /dev/md127
```


## 五、挂载
`sudo mkdir /run/media/shafish/wd4t`
`sudo mount /dev/sda /run/media/shafish/wd4t`

## 更改权限
`sudo chown -R shafish:shafish /run/media/shafish/wd4t`
或者如果存在`plugdev`组，则直接把当前用户添加到该组即可。（用的是manjaro i3wm发现没有这个组，就算了，怎么方便怎么来）

此时直接在`/run/media/shafish/wd4t`目录下进行读写操作即可。

## 六、开机自动挂载
硬盘长时间连接在电脑上，不需要移动的情况下，设置开机自动挂载。
`sudo blkid /dev/sdx` 复制指向sda的uuid
`sudo nano /etc/fstab` 编辑挂载文件，添加下面内容后，保存，重启，ok！！
`UUID=UUID /run/media/shafish/wd4t ext4 defaults 0 0`

## 七、硬盘读写速度测试
### 1.软件读取速度测试
`sudo hdparm -tT /dev/sda`
```shell
# 跟官方标明的平均 111.3MB/sec 有很大出入，待实操测试
# 正常读取速度
Timing buffered disk reads: 116 MB in  3.01 seconds =  38.52 MB/sec
# 缓存读取速度
Timing cached reads:   17418 MB in  1.99 seconds = 8755.33 MB/sec
```
### 2.软件写入速度测试
`sudo dd if=/dev/sda of=/tmp/tempfile bs=1M count=1024`

```shell
$ sudo dd if=/dev/sda of=/tmp/tempfile bs=1M count=1024                                                                                                                                         [9:56:30]
1024+0 records in
1024+0 records out
1073741824 bytes (1.1 GB, 1.0 GiB) copied, 26.6579 s, 40.3 MB/s
```

### 3.实际写入测试
> 固态to新硬盘

```shell
# 第一次
$ sudo rsync -av --progress /run/media/shafish/Windows/Users/shafish/Videos/马赛克.mp4 /run/media/shafish/wd4t/                                                                                [9:50:59]
sending incremental file list
马赛克.mp4
  7,749,298,820 100%   53.58MB/s    0:02:17 (xfr#1, to-chk=0/1)

sent 7,751,190,846 bytes  received 35 bytes  55,965,277.12 bytes/sec
total size is 7,749,298,820  speedup is 1.00
```
```shell
# 同一文件第二次
$ sudo rsync -av --progress /run/media/shafish/Windows/Users/shafish/Videos/马赛克.mp4 /run/media/shafish/wd4t/                                                                                [9:54:08]
sending incremental file list
马赛克.mp4
  7,749,298,820 100%   54.33MB/s    0:02:16 (xfr#1, to-chk=0/1)

sent 7,751,190,846 bytes  received 35 bytes  56,785,281.18 bytes/sec
total size is 7,749,298,820  speedup is 1.00
```

### 4.测试结论
这是一块纯纯的5400转速机械硬盘，实际对一个7G+文件进行写入时，前35%进度因使用到缓存，其传输速度能达到甚至超过官方标明的平均读写速度111MB/sec，后65%基本稳定在了38MB/sec，跟软件测试读写速度一致。

最后给这块硬盘定位：确实跟官方标明信息一致-`入门级nas低功耗存储硬盘`。(带资本家刀法真准)

ps：机械硬盘竖放，平放都阔已（不要斜放就行，否则容易损坏）

ref:

- https://zh.wikipedia.org/wiki/S.M.A.R.T.
