Page({
  data: {
  },
  onShow: function (): void {
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 0
      });
    }
  },
})