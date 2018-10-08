# 硬件分析
硬件采用optane硬盘，读写速度相对较快。
Intel Optane技术结合了目前英特尔在存储研究上最为先进硬件介质和软件方案，其中硬件介质3D XPoint是整个Optane技术的核心。
  在性能方面，16GB版本Optane内存持续读取最高为900MB/s，持续写入最高为145MB/s；4K 随机读取为190000 IOPS，4K随机写入是35000 IOPS。32GB版本持续读取速度为1200MB/s，持续写入最高为280MB/s；4K 随机读取为300000 IOPS，4K随机写入是70000 IOPS。从官方给的数据看，Optane内存不管是持续性能还是随机性能，读取性能均远好于写入性能。
![傲腾](https://images.techhive.com/images/article/2017/03/intel_optane_3-100715136-orig.jpg)
# 软件思路(阶段一)
### 写过程
#### 接收Key和Value
多线程接受信息并写日志
#### key、value落地
treeset存储，多文件存储
### 读过程
#### 接收key
多线程接收
#### 查找
去log查找、去treeset里面查找

<hr>

# 软件思路(阶段二)
### 写过程
#### 接收Key和Value
#### key落地到磁盘
#### key、value落地
### 读过程
#### 接收key
#### 查找key
#### 查找value