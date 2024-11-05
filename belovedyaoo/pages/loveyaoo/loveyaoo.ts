import {
  markers
} from './markers';

Page({
  data: {
    longitude: 111.672373 as number,
    latitude: 29.046068 as number,
    scale: 17 as number,
    markers: markers as Array<any>
  },

  onShow: function (): void {
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 1
      });
    }
  },

  mapP(e:any) {
    console.log(e.detail)
  }

});