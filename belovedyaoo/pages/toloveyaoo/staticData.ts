export const classDataList = [
  {
    className: '软件项目管理',
    classPeriod: '6-13',
    classTeacher: '唐嘉良',
    classLocation: 'T2-D101',
    displayColor: '#82A6BE'
  }, {
    className: '软件项目管理实验',
    classPeriod: '7-14',
    classTeacher: '唐嘉良',
    classLocation: 'P-D706',
    displayColor: '#995053'
  }, {
    className: 'UML建模',
    classPeriod: '6-13',
    classTeacher: '刘波涛',
    classLocation: 'T2-D101',
    displayColor: '#694D52'
  }, {
    className: 'UML建模实验',
    classPeriod: '9-13',
    classTeacher: '刘波涛',
    classLocation: 'P-D706',
    displayColor: '#568394'
  }, {
    className: '算法分析与设计',
    classPeriod: '6-13',
    classTeacher: '佘青',
    classLocation: 'T2-D101',
    displayColor: '#666364'
  }, {
    className: '算法分析与设计实验',
    classPeriod: '8-15',
    classTeacher: '佘青',
    classLocation: 'P-D701',
    displayColor: '#556B7D'
  }, {
    className: '移动开发技术基础',
    classPeriod: '2-4',
    classTeacher: '赵辉',
    classLocation: 'T2-D101',
    displayColor: '#7B886F'
  }, {
    className: '移动开发技术基础实验',
    classPeriod: '等待排课',
    classTeacher: '赵辉',
    classLocation: 'T2-D101',
    displayColor: '#998EBC'
  }, {
    className: '软件质量保证与测试',
    classPeriod: '2-13',
    classTeacher: '巢湘萍',
    classLocation: 'T2-D101',
    displayColor: '#A7815E'
  },
] as Array<classInfoType>;

export const scheduleData = {
  first: [
    classDataList[0],
    classDataList[2],
    classDataList[0],
    {},
    classDataList[3],
    {},
    {}
  ],
  second: [
    classDataList[2],
    classDataList[8],
    classDataList[1],
    classDataList[8],
    classDataList[4],
    {},
    {}
  ],
  third: [
    classDataList[6],
    classDataList[6],
    classDataList[4],
    classDataList[6],
    classDataList[7],
    {},
    {}
  ],
  fourth: [
    classDataList[6],
    classDataList[6],
    {},
    classDataList[6],
    {},
    {},
    {}
  ],
  fifth: [
    {},
    {},
    {},
    {},
    {},
    {},
    {}
  ],
} as scheduleDataType;

export const classTime = {
  one: [{
    index: 1,
    timeStart: '08:00',
    timeEnd: '08:45'
  }, {
    index: 2,
    timeStart: '08:55',
    timeEnd: '09:40'
  }, {
    index: 3,
    timeStart: '10:00',
    timeEnd: '10:45'
  }, {
    index: 4,
    timeStart: '10:55',
    timeEnd: '11:40'
  }],
  two: [{
    index: 5,
    timeStart: '14:30',
    timeEnd: '15:15'
  }, {
    index: 6,
    timeStart: '15:25',
    timeEnd: '16:10'
  },{
    index: 7,
    timeStart: '16:30',
    timeEnd: '17:15'
  }, {
    index: 8,
    timeStart: '17:25',
    timeEnd: '18:10'
  }]
} as classTime;

export const scheduleTable = {
  'Monday' : {
    'First' : classDataList[0],
    'Second': classDataList[2],
    'Third': classDataList[6],
    'Fourth': classDataList[6],
  },
  'Tuesday': {
    'First':classDataList[2],
    'Second':classDataList[8],
    'Third': classDataList[6],
    'Fourth': classDataList[6],
  },
  'Wednesday': {
    'First':classDataList[0],
    'Second':classDataList[1],
    'Third': classDataList[4],
  },
  'Thursday': {
    'Second':classDataList[8],
    'Third': classDataList[6],
    'Fourth': classDataList[6],
  },
  'Friday': {
    'First':classDataList[3],
    'Second':classDataList[4],
    'Third': classDataList[7],
  }
} as ScheduleTable;