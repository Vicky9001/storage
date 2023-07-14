const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  token2: state => state.user.token2,
  userId: state => state.user.userId,
  avatar: state => state.user.avatar,
  userName: state => state.user.userName,
  realName: state => state.user.realName,
  phone: state => state.user.phone,
  password: state => state.user.password,
  roleNames: state => state.user.roleNames,
  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs
}
export default getters
