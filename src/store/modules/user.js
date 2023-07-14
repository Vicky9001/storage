import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken, getToken2, setToken2, removeToken2 } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  userId: null,
  token: getToken(),
  token2: getToken2(),
  userName: '',
  realName: '',
  phone: '',
  password: '',
  roleNames: [],
  avatar: ''
}

const mutations = {
  SET_USERID: (state, userId) => {
    state.userId = userId
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_TOKEN2: (state, token) => {
    state.token2 = token
  },
  SET_USERNAME: (state, userName) => {
    state.userName = userName
  },
  SET_REALNAME: (state, realName) => {
    state.realName = realName
  },
  SET_PHONE: (state, phone) => {
    state.phone = phone
  },
  SET_PASSWORD: (state, password) => {
    state.password = password
  },
  SET_ROLENAMES: (state, roleNames) => {
    state.roleNames = roleNames
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { userName, password } = userInfo
    console.log(userInfo)
    return new Promise((resolve, reject) => {
      login({
        grant_type: 'password',
        client_id: 'smart-hamster',
        client_secret: '123456',
        username: userName.trim(),
        password: password
      }).then(res => {
        const { data } = res
        commit('SET_TOKEN', data.token.token)
        commit('SET_TOKEN2', data.token2)
        console.log(data.token.token)
        console.log(data.token2)
        setToken(data.token.token)
        setToken2(data.token2)
        resolve()
      }).catch(error => {
        reject(error)
      })
      console.log(getToken())
      console.log(state.token)
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token2).then(response => {
        if (!response) {
          reject('Verification failed, please Login again.')
        }
        const { userId, userName, password, realName, phone, roles, avatar } = response.data
        console.log()
        const roleNames = roles.map(role => role.roleName)
        commit('SET_USERID', userId)
        commit('SET_USERNAME', userName)
        commit('SET_PASSWORD', password)
        commit('SET_REALNAME', realName)
        commit('SET_PHONE', phone)
        commit('SET_ROLENAMES', roleNames)
        commit('SET_AVATAR', avatar)
        resolve(response.data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_TOKEN2', '')
        commit('SET_ROLENAMES', [])
        removeToken()
        removeToken2()
        resetRouter()
        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_TOKEN2', '')
      commit('SET_ROLENAMES', [])
      removeToken()
      removeToken2()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
