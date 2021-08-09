import { Enum } from '../../utils/enum'

export default {
  editTypeEnum: new Enum().add('html', 'html', 2).add('markdown', 'markdown', 1),
  articlePermissionEnum: new Enum().add('all', '所有人', 1).add('login', '仅登录', 2).add('private', '私人', 3),
  commentLimitEnum: new Enum().add('enable', '允许', 1).add('disable', '不允许', 0),
  articleStatusEnum: new Enum().add('normal', '正常', 1).add('draft', '草稿', 2).add('delay', '定时', 3).add('deleted', '删除', 0),
  articleTypeEnum: new Enum().add('default', '默认', 1).add('big', '大图', 2).add('left', '左图', 3).add('sidebar', '侧边栏', 4).add('images', '图片模式', 5),
  friendsLinkEnum: new Enum().add('wait', '待审核', 100).add('approve', '审核通过', 200).add('invalidate', '链接失效', 301).add('removed', '被对方移除', 302).add('repeat', '重复申请', 400).add('deny', '审核不通过', 500)
}
