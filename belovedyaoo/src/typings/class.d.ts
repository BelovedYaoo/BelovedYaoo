// 课程表相关的类型定义
// 星期定义
type Week = 'Monday' | 'Tuesday' | 'Wednesday' | 'Thursday' | 'Friday' | 'Saturday' | 'Sunday';
// 节次定义
type Section = 'First' | 'Second' | 'Third' | 'Fourth' | 'Fifth';
// 课程表结构，以星期为键
type ScheduleTable = {
  [week in Week]: CourseDataPerDay;
};
// 每日课程结构，以节次为键
type CourseDataPerDay = {
  [section in Section]: classInfoType | Array<classInfoType> | null;
};
// 转置后的课程表结构，以节次为键，顺序转置无需通过星期来标识，而是通过填入空对象占位，确保课程顺序一致
type scheduleArray = {
  [section in Section]: Array<classInfoType | Array<classInfoType> | {}>
};

type classInfoType = {
  className: string,
  classPeriod: string,
  classTeacher: string,
  classLocation: string,
  displayColor: string
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