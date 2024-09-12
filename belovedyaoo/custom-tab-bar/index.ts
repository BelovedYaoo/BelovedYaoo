type Tabbar = {
  pagePath: string,
  iconPath: string,
  selectedIconPath: string,
  text: string
};

type TabbarList = Array<Tabbar>;

Component({
  properties: {
  },
  data: {
    selected: 0 as number,
    list: [
      {
        "pagePath": "/pages/belovedyaoo/belovedyaoo",
        "iconPath": "/src/images/qianceng.png",
        "selectedIconPath": "/src/images/qianceng.png",
        "text": "想对你说"
      },
      {
        "pagePath": "/pages/loveyaoo/loveyaoo",
        "iconPath": "/src/images/musi.png",
        "selectedIconPath": "/src/images/musi.png",
        "text": "唐诗"
      },
      {
        "pagePath": "/pages/toloveyaoo/toloveyaoo",
        "iconPath": "/src/images/musi.png",
        "selectedIconPath": "/src/images/musi.png",
        "text": "关于我们"
      },
      {
        "pagePath": "/pages/tureloveyaoo/tureloveyaoo",
        "iconPath": "/src/images/dangao.png",
        "selectedIconPath": "/src/images/dangao.png",
        "text": "个人"
      },
      {
        "pagePath": "/pages/youandyaoo/youandyaoo",
        "iconPath": "/src/images/mianbao.png",
        "selectedIconPath": "/src/images/mianbao.png",
        "text": "嘻嘻"
      }
    ] as TabbarList
  },
  methods: {
    switchTab(e: any) {
      const { index, url } = e.currentTarget.dataset;
      if (this.data.selected == index || index == undefined) return;
      this.setData({
        selected: index
      });
      wx.switchTab({
        url
      });
    }
  }
})