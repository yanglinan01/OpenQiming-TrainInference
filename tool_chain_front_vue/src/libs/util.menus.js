function getDynamicMenus () {
  const menus = {
      header: [
          {
              path: '/',
              title: '首页',
              icon: 'md-home',
              hideSider: false,
              name: 'home'
          },
          {
              path: '/log',
              title: '日志',
              icon: 'md-locate',
              hideSider: true,
              name: 'system'
          }
      ],
      sider: [
          {
              path: '/home',
              title: '首页',
              header: 'home',
              icon: 'md-speedometer'
          }
      ]
  }
  return new Promise(resolve => {
      if (sessionStorage.menus) {
        resolve(JSON.parse(sessionStorage.menus))
      } else {
        const loadingDom = document.querySelector('#loading')
        loadingDom.style.display = 'block'
        setTimeout(() => {
          console.log('get success!')
          sessionStorage.menus = JSON.stringify(menus)
          resolve(menus)
        }, 5000)
      }
  })
}

export default getDynamicMenus
