import {
  getCurrWeekList,
  formateDate,
  getCurrentPeriod
} from './util';

import {
  classDataList,
  classTime,
  scheduleTable
} from './staticData';

import ScheduleDataVo from './scheduleVo';

Page({
  data: {
    startTime: new Date('2024-09-02T00:00:00').getTime() as number,
    currentWeek: 1 as number,
    classDataList,
    scheduleData: scheduleTable,
    scheduleDataTranspose: {} as scheduleArray,
    time: classTime,
    isShow67: false,
    weekList: [],
    isShow: false,
    current: {},
  },
  getDetail(e: any) {
    let {
      item
    } = e.currentTarget.dataset;
    if (this.isEmptyObject(item)) {
      return;
    }
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
  isEmptyObject: function (obj: {}) {
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        return false;
      }
    }
    return true;
  },
  onShow() {
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      });
    }
    let time = new Date(),
      list = getCurrWeekList(time),
      weekList = [] as any;

    const firstIsEmpty = this.isEmptyObject(this.data.scheduleData.Saturday);
    const secondIsEmpty = this.isEmptyObject(this.data.scheduleData.Sunday);

    const isPop = firstIsEmpty && secondIsEmpty;
    if (isPop) {
      list.pop();
      list.pop();
    }

    this.setData({
      isShow67: isPop
    });

    list.forEach(item => {
      weekList.push({
        day: [item.split('-')[1], item.split('-')[2]].join('-'),
        weekt: (new Date(item)).getDay(),
        week: '星期' + '日一二三四五六'.charAt((new Date(item)).getDay()),
        isCurr: formateDate(time) == item,
        isHasClass: this.isHasClass()
      })
    });
    this.setData({
      weekList,
    })
  },
  onLoad: function (): void {
    const scheduleData = new ScheduleDataVo(scheduleTable);
    this.setData({
      currentWeek: getCurrentPeriod(this.data.startTime),
      scheduleDataTranspose: scheduleData.convertToArray()
    });
  },
  splitWeek(str: string): number[] {
    const parts = str.split('-');
    const numbers = parts.map(part => parseInt(part, 10));
    return numbers;
  },
  isHasClass(week: number = new Date().getDay() - 1): boolean {
    console.log(week);
    
    return false;
  }
})