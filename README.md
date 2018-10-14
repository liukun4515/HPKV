# 硬件分析
硬件采用optane硬盘，读写速度相对较快。
Intel Optane技术结合了目前英特尔在存储研究上最为先进硬件介质和软件方案，其中硬件介质3D XPoint是整个Optane技术的核心。
  在性能方面，16GB版本Optane内存持续读取最高为900MB/s，持续写入最高为145MB/s；4K 随机读取为190000 IOPS，4K随机写入是35000 IOPS。32GB版本持续读取速度为1200MB/s，持续写入最高为280MB/s；4K 随机读取为300000 IOPS，4K随机写入是70000 IOPS。从官方给的数据看，Optane内存不管是持续性能还是随机性能，读取性能均远好于写入性能。
![傲腾](https://images.techhive.com/images/article/2017/03/intel_optane_3-100715136-orig.jpg)

# 阶段一
### 前期分析
1. 首先检查HashMap的增长状态是否与键值对的数量成正相关，由下列实验可得大致正相关，相关系数为键值对的大小。
<img src="https://www.wangxinshuo.cn/wp-content/uploads/2018/09/myplot.png" alt="" width="640" height="480" class="aligncenter size-full wp-image-437" />
<img src="https://www.wangxinshuo.cn/wp-content/uploads/2018/09/myplot1.png" alt="" width="640" height="480" class="aligncenter size-full wp-image-439" />
2. 部分序列化并写入的策略是可行的：利用序列化之后的byte数组和RandomAccessFile来进行部分重写。（RandomAccessFile是会覆盖原记录的，在使用的时候还需要密切注意文件指针的位置）
** 思路：得到key、value并写入HashMap，将HashMap进行序列化，增量的写入到文件中**
### 已完成的工作：
1. 增量写入
2. 增、查
### 性能
在单线程写10000key与value的情况下耗时7494分钟，遍历完成时间为66.680秒

<br><hr><br>

# 阶段二
已经完成，性能尚可
<a href="https://www.wangxinshuo.cn/2018/10/11/%E5%A4%A9%E6%B1%A0%E6%95%B0%E6%8D%AE%E5%BA%93%E6%AF%94%E8%B5%9B%E5%88%86%E6%9E%90%E4%B8%8E%E5%AE%9E%E7%8E%B0%EF%BC%88%E9%98%B6%E6%AE%B5%E4%BA%8C%EF%BC%89/">阶段二</a>

# 阶段三
准备在阶段二的基础上性能再进一步

<a href="https://www.wangxinshuo.cn/2018/10/14/%E5%A4%A9%E6%B1%A0%E6%95%B0%E6%8D%AE%E5%BA%93%E6%AF%94%E8%B5%9B%E5%88%86%E6%9E%90%E4%B8%8E%E5%AE%9E%E7%8E%B0%EF%BC%88%E9%98%B6%E6%AE%B5%E4%B8%89%EF%BC%89/">阶段三</a>
# 阶段四
应对复赛的方案设计
