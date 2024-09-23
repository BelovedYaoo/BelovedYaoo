// 课程表相关的类型定义 
type classInfoType = {
  className: string,
  classPeriod: string,
  classTeacher: string,
  classLocation: string,
  displayColor: string
};

type scheduleDataType = {
  first: Array<classInfoType | Array<classInfoType> | {}>,
  second: Array<classInfoType | Array<classInfoType> | {}>,
  third: Array<classInfoType | Array<classInfoType> | {}>,
  fourth: Array<classInfoType | Array<classInfoType> | {}>,
  fifth: Array<classInfoType | Array<classInfoType> | {}>,
};

type timeInfo = {
  index: number,
  timeStart: string,
  timeEnd: string
};

type classTime = {
  one: Array<timeInfo>,
  two: Array<timeInfo>,
  three: Array<timeInfo>
};