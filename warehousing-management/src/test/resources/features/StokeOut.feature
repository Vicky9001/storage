#language:zh-CN
功能: 出库管理

  场景: 获取出库单列表
    假如我希望获取出库单列表
    当我使用类型 1 调用getOrder
    那么返回的结果应为成功，包含一组OutRecord，类型为 1,尺寸为 3


  场景: 更新出库状态
    假如我有一个id为 22 、操作类型state为 16 、operator为 1 、remark为 "test" 的Map对象
    当我使用该Map对象调用changeState
    那么返回的结果应为拣货确认成功