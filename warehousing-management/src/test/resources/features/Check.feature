#language:zh-CN
功能: 盘点管理

  场景: 获取盘点单列表
    假如我有一组CheckRecords
    当我使用类型 3 和创建人ID 1 调用getOrder
    那么返回的结果应为成功，包含一组CheckRecord，类型为 3，创建人ID为 1

  场景: 创建一个盘点任务
    假如我有一个creatorId为 1、positionId为 8、描述为 "测试记录" 的Map对象
    当我使用该Map对象调用createOrder
    那么返回的结果应为成功，包含一个CheckRecord

  场景: 更新盘点记录
    假如我有一个orderId为 4 、盘点结果realNum为 45的Map对象
    当我使用该Map对象调用updateOrder
    那么返回的结果应为成功，包含一个更新后的CheckRecord

  场景: 更新盘点记录，但输入的realNum不合法
    假如我输入的realNum不合法，比如orderId为 4 、realNum超出货架容量 100
    当我使用这些不合法的参数调用updateOrder
    那么返回的结果应为失败，包含一个错误提示信息