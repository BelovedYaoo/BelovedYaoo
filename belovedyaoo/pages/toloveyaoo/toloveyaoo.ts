import {
  getCurrWeekList,
  formateDate,
  getCurrentPeriod
} from './util';

import {
  classDataList,
  scheduleData,
  classTime,
  scheduleTable
} from './staticData';

import ScheduleDataVo from './scheduleVo';

Page({
  data: {
    startTime: new Date('2024-09-02T00:00:00').getTime() as number,
    currentWeek: 1 as number,
    classDataList,
    scheduleData,
    scheduleDataTranspose: new ScheduleDataVo(scheduleTable),
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

    const firstIsEmpty = (this.isEmptyObject(scheduleData.first[5]) && this.isEmptyObject(scheduleData.first[6]));
    const secondIsEmpty = (this.isEmptyObject(scheduleData.second[5]) && this.isEmptyObject(scheduleData.second[6]));
    const thirdIsEmpty = (this.isEmptyObject(scheduleData.third[5]) && this.isEmptyObject(scheduleData.third[6]));
    const fourthIsEmpty = (this.isEmptyObject(scheduleData.fourth[5]) && this.isEmptyObject(scheduleData.fourth[6]));
    const fifthIsEmpty = (this.isEmptyObject(scheduleData.fifth[5]) && this.isEmptyObject(scheduleData.fifth[6]));

    const isPop = firstIsEmpty && secondIsEmpty && thirdIsEmpty && fourthIsEmpty && fifthIsEmpty;
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
        week: "星期" + "日一二三四五六".charAt((new Date(item)).getDay()),
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
    console.log(scheduleData.convertScheduleToNewFormat());
    this.setData({
      currentWeek: getCurrentPeriod(this.data.startTime)
    });
  },
  splitWeek(str: string): number[] {
    const parts = str.split('-');
    const numbers = parts.map(part => parseInt(part, 10));
    return numbers;
  },
  isHasClass(week: number = new Date().getDay() - 1): boolean {
    const classDataList: Array<classInfoType | {}> = [];

    classDataList.push(this.data.scheduleData.first[week]);
    classDataList.push(this.data.scheduleData.second[week]);
    classDataList.push(this.data.scheduleData.third[week]);
    classDataList.push(this.data.scheduleData.fourth[week]);
    classDataList.push(this.data.scheduleData.fifth[week]);

    let classPeriod: number[] | undefined;

    for (const item of classDataList) {
      if (item instanceof Object && 'classPeriod' in item) {
        const periodStr = (item as classInfoType).classPeriod;
        classPeriod = this.splitWeek(periodStr) as number[];

        if (classPeriod && classPeriod.length === 2 &&
          classPeriod[0] <= this.data.currentWeek && this.data.currentWeek <= classPeriod[1]) {
          return true;
        }
      }
    }

    return false;
  }
})