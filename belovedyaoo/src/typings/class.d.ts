// 课程表相关的类型定义
type Week = 'Monday' | 'Tuesday' | 'Wednesday' | 'Thursday' | 'Friday' | 'Saturday' | 'Sunday';

type Section = 'First' | 'Second' | 'Third' | 'Fourth' | 'Fifth';

type CourseDataPerDay = {
  [section in Section]: classInfoType | Array<classInfoType> | null;
};

type ScheduleTable = {
  [week in Week]: CourseDataPerDay;
};

type classInfoType = {
  className: string,
  classPeriod: string,
  classTeacher: string,
  classLocation: string,
  displayColor: string
};

type scheduleArray = {
  [section in Section]: Array<classInfoType | Array<classInfoType> | {}>
};

type timeInfo = {
  index: number,
  timeStart: string,
  timeEnd: string
};

type classTime = {
  one: Array<timeInfo>,
  two: Array<timeInfo>
};