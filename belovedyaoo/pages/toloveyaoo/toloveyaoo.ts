import {
  getCurrWeekList,
  formateDate
} from './util';

import {
  classDataList,
  scheduleData
} from './staticData';

Page({
  data: {
    startTime: new Date('2024-09-02T00:00:00').getTime() as number,
    currentWeek: 1 as number,
    classDataList,
    scheduleData,
    time: {
      one: [{
        index: 1,
        timeStart: '08:00',
        timeEnd: '08:45'
      },
      {
        index: 2,
        timeStart: '08:55',
        timeEnd: '09:40'
      },
      {
        index: 3,
        timeStart: '10:00',
        timeEnd: '10:45'
      },
      {
        index: 4,
        timeStart: '10:55',
        timeEnd: '11:40'
      }
      ],
      two: [{
        index: 5,
        timeStart: '14:30',
        timeEnd: '15:15'
      },
      {
        index: 6,
        timeStart: '15:25',
        timeEnd: '16:10'
      },
      ],
      three: [{
        index: 7,
        timeStart: '16:30',
        timeEnd: '17:15'
      },
      {
        index: 8,
        timeStart: '17:25',
        timeEnd: '18:10'
      },
      ]
    },
    weekList: [],
    isShow: false,
    current: {},
  },
  getDetail(e: any) {
    let {
      item
    } = e.currentTarget.dataset;
    console.log(item)
    this.setData({
      current: item,
      isShow: true
    })
  },
  close() {
    this.setData({
      isShow: false
    })
  },
  onShow() {
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      });
    }
    let time = new Date(),
      list = getCurrWeekList(time),
      weekList = [] as any
    list.forEach(item => {
      weekList.push({
        day: [item.split('-')[1], item.split('-')[2]].join('-'),
        week: "星期" + "日一二三四五六".charAt((new Date(item)).getDay()),
        isCurr: formateDate(time) == item
      })
    });
    this.setData({
      weekList,
    })
  },
  onLoad: function (): void {
    var diffInMs = Math.abs(new Date().getTime() - this.data.startTime);
    var diffInWeeks = diffInMs / (1000 * 60 * 60 * 24 * 7);
    this.setData({
      currentWeek: Math.ceil(diffInWeeks)
    });
  }
})