import {
  getCurrWeekList,
  formateDate,
  getCurrentPeriod
} from './util';

import {
  classDataList,
  classTime,
  scheduleTable,
  WeekConst,
  SectionConst
} from './staticData';

import ScheduleDataVo from './scheduleVo';

Page({
  data: {
    startTime: new Date('2024-09-02T00:00:00').getTime() as number,
    currentWeek: 1 as number,
    classDataList,
    scheduleData: {} as ScheduleDataVo,
    scheduleTable,
    scheduleDataTranspose: new ScheduleDataVo(scheduleTable).convertToArray() as scheduleArray,
    time: classTime,
    isShow67: false,
    weekList: [],
    isShow: false,
    current: {},
    displayData: [] as Array<shecduleDataDisplay>
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
    this.initDisplayData()
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      });
    }
    let time = new Date(),
      list = getCurrWeekList(time),
      weekList = [] as any;

    const firstIsEmpty = this.isEmptyObject(this.data.scheduleTable.Saturday);
    const secondIsEmpty = this.isEmptyObject(this.data.scheduleTable.Sunday);

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
        week: '星期' + '日一二三四五六'.charAt((new Date(item)).getDay()),
        isCurr: formateDate(time) == item,
        isHasClass: this.isHasClass((new Date(item)).getDay())
      })
    });
    this.setData({
      weekList,
    })
  },
  onLoad: function (): void {
    this.setData({
      currentWeek: getCurrentPeriod(this.data.startTime),
      scheduleData: new ScheduleDataVo(scheduleTable),
    });
  },
  splitWeek(str: string): number[] {
    const parts = str.split('-');
    const numbers = parts.map(part => parseInt(part, 10));
    return numbers;
  },
  isHasClass(week: number = new Date().getDay()): boolean {
    for (const section of SectionConst) {
      const classInfo = this.data.scheduleData.getCourse(WeekConst[week], section) as classInfoType;
      if (classInfo !== null) {
        const periodStr = classInfo.classPeriod;
        const classPeriod = this.splitWeek(periodStr) as number[];
        if (classPeriod && classPeriod.length === 2 &&
          classPeriod[0] <= this.data.currentWeek && this.data.currentWeek <= classPeriod[1]) {
          return true;
        }
      }
    }
    return false;
  },
  initDisplayData() {
    let displayDataTemp = [] as Array<shecduleDataDisplay>;
    let cycleCount = 0;

    const getCourseDataForCycleCount = (cycleCount: number): Array<courseDataDisplay> => {
      if (cycleCount === 0) {
        return [
          this.data.scheduleDataTranspose.First,
          this.data.scheduleDataTranspose.Second
        ]
      } else {
        return [
          this.data.scheduleDataTranspose.Third,
          this.data.scheduleDataTranspose.Fourth
        ]
      }
    }

    for (const key of Object.keys(this.data.time) as (keyof classTime)[]) {
      displayDataTemp.push({
        timeDisplay: this.data.time[key],
        courseDataDisplay: getCourseDataForCycleCount(cycleCount)
      });
      cycleCount++;
    }
    cycleCount = 0;
    this.setData({
      displayData: displayDataTemp
    });
  }
})