import { Enum } from '../../utils/enum'

export default {
  editTypeEnum: new Enum().add('html', 'html', 2).add('markdown', 'markdown', 1),
  articlePermissionEnum: new Enum().add('all', '所有人', 1).add('login', '仅登录', 2).add('private', '私人', 3),
  commentLimitEnum: new Enum().add('enable', '允许', 1).add('disable', '不允许', 0),
  articleStatusEnum: new Enum().add('normal', '正常', 1).add('draft', '草稿', 2).add('delay', '定时', 3).add('deleted', '删除', 0),
  articleTypeEnum: new Enum().add('default', '默认', 1).add('big', '大图', 2).add('left', '左图', 3).add('sidebar', '侧边栏', 4).add('images', '图片模式', 5)
}
