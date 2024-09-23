export const getCurrWeekList = (data: Date) => {
  //根据日期获取本周周一~周日的年-月-日
  let weekList = [],
    date = new Date(data);
  //获取当前日期为周一的日期
  date.setDate(date.getDay() == 0 ? date.getDate() - 6 : date.getDate() - date.getDay() + 1);
  // push周一数据
  weekList.push(formateDate(date));
  console.log(weekList)
  //push周二以后日期
  for (var i = 0; i < 6; i++) {
    date.setDate(date.getDate() + 1);
    weekList.push(formateDate(date));
  }
  return weekList;
};

/**
 * 格式化日期
 * @param {*} time 
 */
export const formateDate = (time: Date) => {
  let year = time.getFullYear();
  let month = time.getMonth() + 1 < 10 ? '0' + (time.getMonth() + 1) : (time.getMonth() + 1);
  let day = time.getDate() < 10 ? '0' + time.getDate() : time.getDate();
  return year + '-' + month + '-' + day;
};

/**
 * 传入一个开始时间，计算距离开始时间过去了多少周
 * @param startTime 开始时间
 */
export const getCurrentPeriod = (startTime: number): number => {
  const diffInMs = Math.abs(new Date().getTime() - startTime);
  const diffInWeeks = diffInMs / (1000 * 60 * 60 * 24 * 7);
  return Math.ceil(diffInWeeks);
};