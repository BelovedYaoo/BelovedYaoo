import { getCurrentPeriod } from './util';

class ScheduleDataVo {

  constructor(private scheduleTable: ScheduleTable = createEmptyScheduleTable()) {
    this.scheduleTable = scheduleTable;
  }

  splitWeek(str: string): number[] {
    const parts = str.split('-');
    const numbers = parts.map(part => parseInt(part, 10));
    return numbers;
  }

  // 获取特定星期与节次的课程
  getCourse(week: Week, section: Section): classInfoType | null {
    // 检查 scheduleTable 中是否存在该星期的每日课程数据
    if (!(week in this.scheduleTable)) {
      return null;
    }
    // 取出该星期的每日课程数据
    const daySchedule = this.scheduleTable[week];
    // 检查每日课程数据中是否存在该节次的课程数据
    if (!(section in daySchedule)) {
      return null;
    }
    // 取出该节次的课程数据
    const courseOrCourses = daySchedule[section];
    if (courseOrCourses === null) {
      return null;
    }
    // 如果是数组，代表该星期该节次下存在多个课程数据，需要进行周次判断
    if (Array.isArray(courseOrCourses)) {
      let classPeriod: number[] | undefined;
      const currentPeriod = getCurrentPeriod(new Date('2024-09-02T00:00:00').getTime());
      for (const course of courseOrCourses) {
        const periodStr = (course as classInfoType).classPeriod;
        classPeriod = this.splitWeek(periodStr) as number[];
        if (classPeriod && classPeriod.length === 2 &&
          classPeriod[0] <= currentPeriod && currentPeriod <= classPeriod[1]) {
          return course;
        }
      }
      return null;
    }
    // 单个课程对象直接返回
    return courseOrCourses;
  }

  // 获取特定周次的所有课程
  getCoursesOfDay(week: Week): CourseDataPerDay {
    return this.scheduleTable[week] || {};
  }

  convertToArray(): scheduleArray {
    const newFormat: scheduleArray = {
      First: [],
      Second: [],
      Third: [],
      Fourth: [],
      Fifth: []
    };

    const sections = ['First', 'Second', 'Third', 'Fourth', 'Fifth'];

    // 遍历所有可能的时间段
    for (const section of sections as Section[]) {
      // 遍历一周中的每一天
      for (const day of Object.values(this.scheduleTable)) {
        let courseInfo = day[section];
        if (courseInfo === undefined) {
          courseInfo = {} as classInfoType;
        }

        newFormat[section].push(courseInfo as classInfoType | {});
      }
    }

    return newFormat;
  }

}

// 构造一个空的ScheduleTable类型数据
function createEmptyScheduleTable(): ScheduleTable {
  const sections: Section[] = ['First', 'Second', 'Third', 'Fourth', 'Fifth'];
  const weekdays: Week[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  const emptySectionRow: CourseDataPerDay = sections.reduce((acc, section) => ({
    ...acc,
    [section]: null,
  }), {} as CourseDataPerDay);

  const emptyScheduleTable: ScheduleTable = weekdays.reduce((acc, day) => ({
    ...acc,
    [day]: { ...emptySectionRow },
  }), {} as ScheduleTable);

  return emptyScheduleTable;
}

export default ScheduleDataVo;