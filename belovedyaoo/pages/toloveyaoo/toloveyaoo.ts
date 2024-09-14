type TimeDiff = {
  hours: number | string;
  minutes: number | string;
  seconds: number | string;
};

type swiperInfo = {
  type: string,
  url: string,
  zIndex?: number,
  mLeft?: number
};

type swiperList = Array<swiperInfo>;

Page({
  data: {
    babyName: "佳瑶" as string,
    startTime: new Date('2024-07-15T00:00:00').getTime() as number,
    currentTime: new Date().getTime() as number,
    timeDiff: {
      hours: 1292,
      minutes: 56,
      seconds: 16
    } as TimeDiff,
    yiyanOpacity: 1 as number,
    yiyan: "我不说想你，月亮会带给你" as string,
    yiyanUpdateErrorCount: 0 as number,
    touchStart: 0 as number,
    direction: 'left' as string,
    swiperList: [
      {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e5460419a90.jpg'
      }, {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e5460450857.jpg'
      }, {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e5460463c7f.jpg',
      }, {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e5460450026.jpg'
      }, {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e546043e1ca.jpg'
      }, {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e5460b9c343.jpg'
      }, {
        type: 'image',
        url: 'https://img.picui.cn/free/2024/09/14/66e5460c64316.jpg'
      }
    ] as swiperList
  },

  onLoad: function (): void {
    this.init();
    this.touchSwiperInit();
  },

  onShow: function (): void {
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      });
    }
  },

  onHide: function (): void {
    clearInterval(this.timer);
  },

  timer: 0 as number,

  init: function (): void {
    this.updateTimer();
    this.updateYiyan();
  },

  updateTimer: function (): void {
    this.timer = setInterval(() => {
      const now = new Date().getTime() as number;
      const diff = now - this.data.startTime as number;

      const hours = Math.floor(diff / (1000 * 60 * 60)) as number;
      const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60)) as number;
      const seconds = Math.floor((diff % (1000 * 60)) / 1000) as number;

      const formattedTime = {
        hours: this.padZero(hours),
        minutes: this.padZero(minutes),
        seconds: this.padZero(seconds),
      } as TimeDiff;

      this.setData({
        timeDiff: formattedTime,
      });
    }, 1000);
  },

  updateYiyan: function (): void {
    wx.request({
      url: 'https://api.vvhan.com/api/text/love',
      method: 'GET',
      success: (requestRes: any): void => {
        if (requestRes.data.length > 20) {
          this.updateYiyan();
          return;
        }
        this.setData({
          yiyanOpacity: 0
        });
        setTimeout(() => {
          this.setData({
            yiyan: requestRes.data,
          });
          this.setData({
            yiyanOpacity: 1
          });
        }, 500);
        setTimeout(this.updateYiyan, 5000);
      },
      fail: (err): void => {
        this.data.yiyanUpdateErrorCount++;
        if (this.data.yiyanUpdateErrorCount <= 5) {
          setTimeout(this.updateYiyan, 5000);
        } else {
          console.error('一言更新错误次数过多，请检查');
        }
        console.error(err);
      },
      complete: (): void => {
      }
    });

  },

  padZero(num: number): string {
    return num.toString().padStart(2, '0');
  },

  autoSwiper: 0 as number,

  touchSwiperInit: function (): void {
    let list = this.data.swiperList;
    for (let i = 0; i < list.length; i++) {
      list[i].zIndex = parseInt((list.length / 2).toString()) + 1 - Math.abs(i - parseInt((list.length / 2).toString()))
      list[i].mLeft = i - parseInt((list.length / 2).toString())
    }
    this.setData({
      swiperList: list
    })
    this.autoSwiper = setTimeout(() => {
      this.touchEnd();
    }, 5000);
  },

  touchStart: function (e: any): void {
    this.setData({
      touchStart: e.touches[0].pageX
    })
  },

  touchMove: function (e: any): void {
    this.setData({
      direction: e.touches[0].pageX - this.data.touchStart > 0 ? 'right' : 'left'
    })
  },

  touchEnd: function (): void {
    clearInterval(this.autoSwiper);
    let direction = (this.data.direction !== undefined && this.data.direction !== null) ? this.data.direction : 'right';
    let list = this.data.swiperList;
    if (direction == 'right') {
      let mLeft = list[0].mLeft;
      let zIndex = list[0].zIndex;
      for (let i = 1; i < list.length; i++) {
        list[i - 1].mLeft = list[i].mLeft
        list[i - 1].zIndex = list[i].zIndex
      }
      list[list.length - 1].mLeft = mLeft;
      list[list.length - 1].zIndex = zIndex;
    } else {
      let mLeft = list[list.length - 1].mLeft;
      let zIndex = list[list.length - 1].zIndex;
      for (let i = list.length - 1; i > 0; i--) {
        list[i].mLeft = list[i - 1].mLeft
        list[i].zIndex = list[i - 1].zIndex
      }
      list[0].mLeft = mLeft;
      list[0].zIndex = zIndex;
    }
    this.setData({
      swiperList: list
    })
    this.autoSwiper = setInterval(this.touchEnd, 5000);
  }

});