import {
  getCurrWeekList,
  formateDate
} from './util';

import {
  classDataList,
  scheduleData
} from './staticData';

import { classInfoType } from "../../src/typings/class";

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
    var diffInMs = Math.abs(new Date().getTime() - this.data.startTime);
    var diffInWeeks = diffInMs / (1000 * 60 * 60 * 24 * 7);
    this.setData({
      currentWeek: Math.ceil(diffInWeeks)
    });
  },
  splitWeek(str: string): number[] {
    const parts = str.split('-');
    const numbers = parts.map(part => parseInt(part, 10));
    return numbers;
  },
  isHasClass(): boolean {
    const week = new Date().getDay() - 1;
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